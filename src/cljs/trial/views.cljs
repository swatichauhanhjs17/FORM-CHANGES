(ns trial.views
  (:require
    [re-frame.core :as re-frame]
    [trial.subs :as subs]
    [reagent.core :as r]
    [trial.specs :as sc]
    [soda-ash.core :as sa]
    [form-validator.core :as form-validator]
    [clojure.spec.alpha :as s]))



(defn my-country
  [final error]
  [sa/FormField {:error error}
   [:label "COUNTRY"]
   [:input {:type        "text"
            :placeholder "COUNTRY"
            :value       (get @final :country)
            :on-change   #(swap! final assoc :country (-> % .-target .-value))}]
   (when error
     [sa/Label {:basic    true
                :color    "red"
                :pointing true} error])])


(defn my-identity
  [final error]
  [sa/FormField {:error error}
   [:label "NAME"]
   [:input {:type      "text"
            :name        :identity
            :placeholder "NAME"
            :value     (get @final :identity)
            :on-change #(swap! final assoc :identity (-> % .-target .-value))}]
   (when error
     [sa/Label {:basic    true
                :color    "red"
                :pointing true} error])])

(defn my-sub
  [final]

  [sa/FormField {:label   " SCIENCE"
                 :control "input"
                 :type    "checkbox"}]

  [sa/FormField {:label   " ARTS"
                 :control "input"
                 :type    "checkbox"}]
  )

(defn my-number
  [final error]
  [sa/FormInput {:error error}
   [:label  "NUMBER"]
   [:input {:type "number"
            :placeholder "NUMBER"
            :name :number
            :value     (get @final :number)
            :on-change #(swap! final assoc :number (-> % .-target .-value))}]

   (when error
     [sa/Label {:basic    true
                :color    "red"
                :pointing true} error])])



(defn my-email
  [final error form]
  [sa/FormField {:error error}
   [:label "Email"]
   [:input {:type        "text"
            :name        :email
            :placeholder "Email"
            :value       (get @final :email)
            :on-change   #(do
                            (swap! final assoc :email (-> % .-target .-value))
                            (form-validator/event->names->value! form %))}]
   (when error
     [sa/Label {:basic    true
                :color    "red"
                :pointing true} error])])


(defn today-date

  [final]
  [sa/FormInput
   {:label     "DOB"
    :input     "date"
    :value     (get @final :date)
    :on-change #(swap! final assoc :date (-> % .-target .-value))}])


(defn my-form
  []
  (let [final (r/atom {:identity "  NAME"
                       :country  " Country"
                       :number   "9876543210"
                       :email    "abc@gmail.com"
                       :date     "01/12/19"
                       })
        error (r/atom {:error1 "ENTER THE EMAIL HERE"
                       :error2 "ENTER YOUR COUNTRY HERE"
                       :error3 " ENTER NAME"
                       :error4 "ENTER  NO."})
        spec->msg {::sc/email "Typo? It doesn't look valid."
                   ::sc/identity "ONLY STRINGS ALLOWED"
                   ::sc/number "can not exceed 10 digits"}
        form-conf {:names->value {:email "abc@gmail.com"} :form-spec ::sc/form}
        form (form-validator/init-form form-conf)
        ]
    (fn []
      [sa/Form {}
       [my-identity final (:error3 @error) form]
       [my-country final (:error2 @error)form]
       [my-number final (:error4 @error) form]
       [my-email final (:error1 @error) form]
       [today-date final]


       [sa/Button {
                   :circular true
                   :on-click #(if (form-validator/form-valid? form)
                                (re-frame/dispatch [:submit @final])
                                (swap! error assoc :error1 (form-validator/get-message form :email spec->msg)))} " SUBMIT"]
       ]
      )))

(defn show-result
  [last-submitted]
  [sa/Item {}
   [sa/ItemImage {}
    [sa/Icon {:size "big" :name "phone"}]]
   [sa/ItemContent {}
    [sa/ItemHeader {} "YOUR NAME :- " (get last-submitted :identity)]
    ]
   [sa/ItemContent {}
    [sa/ItemHeader {} "YOUR COUNTRY:- " (get last-submitted :country)]]
   [sa/ItemContent {}
    [sa/ItemHeader {} "YOUR Number :- " (get last-submitted :number)]
    ]
   [sa/ItemContent {}
    [sa/ItemHeader {} "YOUR Email :- " (get last-submitted :email)]
    ]
   [sa/ItemContent {}
    [sa/ItemHeader {} "Date :- " (get last-submitted :date)]
    ]
   ])

(defn show-all-values
  [all-values]
  [sa/ItemGroup {:ordered true
                 :divided true
                 :style   {:overflow "auto" :max-height "500px"}}
   (for [item all-values]
     ^{:key (str item)} [show-result item]

     )]
  )


(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        all-values (re-frame/subscribe [::subs/all-values])
        last-submitted (re-frame/subscribe [::subs/last-submitted])
        ]

    [sa/Grid {:centered true :columns 2}
     [sa/GridRow {}
      [sa/GridColumn {} [sa/Segment {:placeholder true} [my-form]]]]
     [sa/GridRow {}
      [sa/GridColumn {} [sa/Segment {} [sa/ItemGroup {}
                                        [sa/Item {}
                                         [sa/ItemContent {}
                                          [sa/ItemHeader {} "RECENT DATA"]]]

                                        [show-result @last-submitted]]]]]
     [sa/GridRow {}
      [sa/GridColumn {} [sa/Segment {} [show-all-values @all-values]]]]]

    ))

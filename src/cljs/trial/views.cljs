(ns trial.views
  (:require
    [re-frame.core :as re-frame]
    [trial.subs :as subs]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [form-validator.core :as form-validator]))



(defn my-country
  [final error]
  [sa/FormField {:error error}
   [:label "COUNTRY"]
   [:input {:type "text"
            :placeholder "COUNTRY"
            :value     (get @final :country)
            :on-change #(swap! final assoc :country (-> % .-target .-value))}]
   (when error
     [sa/Label {:basic true
<<<<<<< HEAD
                :color "red"
                :pointing true} error])])
=======
              :color "red"
              :pointing true} error])])
>>>>>>> 4707ba859c661b300ee2851e493d516d13a887e1


(defn my-identity
  [final error]
  [sa/FormFeild {:error error1}
   [:label     "NAME"]
   [    :input  {:type "text"
                 :value     (get @final :identity)
                 :on-change #(swap! final assoc :identity (-> % .-target .-value)) }   ]])

(defn my-sub
  [final]

  [sa/FormFeild {:label " SCIENCE"
                 :control "input"
                 :type "checkbox"}]

  [sa/FormFeild {:label " ARTS"
                 :control "input"
                 :type "checkbox"}]
  )

(defn my-number
  [final error]
  [sa/FormInput
   {:label     "NUMBER"
    :input     "number"
    :value     (get @final :number)
    :on-change #(swap! final assoc :number (-> % .-target .-value))}])


(defn my-email
  [final]
  [sa/FormInput
   {:label     "Email"
    :input     "text"
    :value     (get @final :email)
    :on-change #(swap! final assoc :email(-> % .-target .-value))}])


(defn today-date
  [final]
  [sa/FormInput
   {:label     "DOB"
    :input     "date"
    :value     (get @final :date)
    :on-change #(swap! final assoc :date(-> % .-target .-value))}])



(defn my-form
  []
  (let [final (r/atom {:identity "  NAME"
                       :country  " Country"
                       :number "9876543210"
                       :email "abc@gmail.com"
                       :date "01/12/19"
                       })
        error (r/atom {:error1 "ENTER THE NAME HERE"
                       :error2 "ENTER YOUR COUNTRY HERE"})]
    (fn []
      [sa/Form {}
       [my-identity final error]
       [my-country final (:error2 @error)]
       [my-number final]
       [my-email final]
       [today-date final]
       [sa/Button {
                   :circular true
                   :on-click #(re-frame/dispatch [:submit @final])} " SUBMIT"]
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
      [sa/GridColumn {} [sa/Segment {} [show-all-values @all-values]]]]

     ]

    ))
(ns trial.views
  (:require
    [re-frame.core :as re-frame]
    [trial.subs :as subs]
    [reagent.core :as r]
    [soda-ash.core :as sa]))



(defn my-country
  [final]
  [sa/FormInput {:label     "COUNTRY  "

                 :align     "center"
                 :input     "text"
                 :value     (get @final :country)
                 :on-change #(swap! final assoc :country (-> % .-target .-value))}])


(defn my-identity
  [final]
  [sa/FormInput
   {:label     "NAME"

    :input     "text"
    :value     (get @final :identity)
    :on-change #(swap! final assoc :identity (-> % .-target .-value))}])

(defn my-number
  [final]
  [sa/FormInput
   {:label     "NUMBER"
    :input     "number"
    :value     (get @final :number)
    :on-change #(swap! final assoc :number (-> % .-target .-value))}])



(defn my-form
  []
  (let [final (r/atom {:identity "  NAME"
                       :country  " Country"
                       :number "9876543210"})]
    (fn []
      [sa/Form {}
       [my-identity final]
       [my-country final]
       [my-number final]
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



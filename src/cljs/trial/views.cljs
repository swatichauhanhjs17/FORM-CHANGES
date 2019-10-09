(ns trial.views
  (:require
    [re-frame.core :as re-frame]
    [trial.subs :as subs]
    [reagent.core :as r]
    [soda-ash.core :as sa]))

[sa/Grid {:centered       true
          :columns        3
          :container      true
          :vertical-align "middle"}

 [sa/GridColumn {  } (defn my-country
                       [final]
                       [sa/FormInput{:label "COUNTRY  "
                                     :width "5"
                                     :align "center"
                                     :input  "text"
                                     :value (get @final :country)
                                     :on-change #(swap! final assoc :country (-> % .-target .-value))} ] )

  (defn my-identity
    [final]
    [sa/FormInput
     { :label "NAME"
      :input-Placeholder "NAME"
      :width "5"
      :input "text"
      :value (get @final :identity)
      :on-change #(swap! final assoc :identity (-> % .-target .-value))} ] )

  (defn my-form
    []
    (let [final (r/atom {:identity "  NAME"
                         :country  " Country"})]
      (fn []
        [:div
         [sa/Form {}
          [:h3  [my-identity final]]
          [:h3  [my-country final]]

          [sa/Button {
                      :circular true
                      :on-click #(re-frame/dispatch [:submit @final])} " SUBMIT"] ]
         ]
        )))

  ] [sa/Grid {:text-align "center"
               :centered       true
               :columns        3
               :container      true
               :vertical-align "middle"}
      [sa/GridRow {:columns        2
                   :vertical-align "middle"
                   :divided true}
       [sa/GridColumn {  } (defn show-result
                             [last-submitted ]

                             [:div
                              [:h3 "YOUR NAME :- " (get last-submitted :identity)  ]
                              [:h3  "YOUR COUNTRY:- " (get last-submitted :country) ]] )]
       [sa/GridColumn {  } (defn show-all-values
                             [all-values]
                             [sa/ListSA {:ordered true}
                              (for [item all-values]
                                ^{:key (str item)}
                                [sa/ListItem  {:active true} [sa/ListIcon {:className "marker"}] [show-result item]]
                                ) ]
                             )]]
      ]




 (defn main-panel []
   (let [name (re-frame/subscribe [::subs/name])
         all-values (re-frame/subscribe [::subs/all-values])
         last-submitted (re-frame/subscribe [::subs/last-submitted])
         ]


     [:div

      [my-form]
      [:div[:h3 "RECENT DATA \n" [show-result @last-submitted]]]

       [show-all-values @all-values  ]]

     ))
 ]
















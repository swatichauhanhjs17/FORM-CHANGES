(ns trial.views
  (:require
    [re-frame.core :as re-frame]
    [trial.subs :as subs]
    [reagent.core :as r]
    [soda-ash.core :as sa]))



(defn my-country
  [final]
  [sa/FormInput{:label "COUNTRY  "

                :align "center"
                :input  "text"
                :value (get @final :country)
                :on-change #(swap! final assoc :country (-> % .-target .-value))} ] )


(defn my-identity
  [final]
  [sa/FormInput
   { :label "NAME"

    :input "text"
    :value (get @final :identity)
    :on-change #(swap! final assoc :identity (-> % .-target .-value))} ] )



(defn my-form
  []
  (let [final (r/atom {:identity "  NAME"
                       :country  " Country"})]
    (fn []
      [sa/Item{}
       [sa/Form {}
        [sa/ItemHeader {} [my-identity final]]
        [sa/ItemHeader {} [my-country final]]

        [sa/Button {
                    :circular true
                    :on-click #(re-frame/dispatch [:submit @final])} " SUBMIT"] ]
       ]
      )))
(defn show-result
  [last-submitted ]
  [sa/Item {}
   [sa/ItemHeader {} "YOUR NAME :- " (get last-submitted :identity)  ]
   [ sa/ItemContent {} "YOUR COUNTRY:- " (get last-submitted :country) ]] )

(defn show-all-values
  [all-values]
  [sa/ItemGroup {:ordered true
                 :divided true
              :style {:overflow "auto" :max-height "500px"}}
   (for [item all-values]
     ^{:key (str item)}[show-result item]

     ) ]
  )





 (defn main-panel []
   (let [name (re-frame/subscribe [::subs/name])
         all-values (re-frame/subscribe [::subs/all-values])
         last-submitted (re-frame/subscribe [::subs/last-submitted])
         ]


     [sa/Grid {
               :columns        2

               :vertical-align "top"}
      [sa/GridRow {:columns      2
                   :vertical-align "top"
                   :divided true
                   :stackable true
                   :stretched true}
       [sa/GridColumn { :stretched true } [sa/Segment {}[my-form] ]  ]]
       [sa/GridRow {:columns      4
                    :vertical-align "top"
                    :divided true
                    :stackable true
                    :stretched true}
        [sa/GridColumn { :stretched true }[sa/Segment {} [sa/ItemHeader{} "RECENT DATA \n" [show-result @last-submitted]]]] ]
    [sa/GridRow {:columns      2
                 :vertical-align "top"
                 :divided true
                 :stackable true
                 :stretched true} [sa/GridColumn { :stretched true } [sa/Segment {}[show-all-values @all-values  ]] ] ]





       ]

     ))















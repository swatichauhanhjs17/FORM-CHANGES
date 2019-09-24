(ns trial.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))




(re-frame/reg-sub
  ::last-submitted
  (fn [db]
   (last (:all-values db)
)))


(re-frame/reg-sub
  ::all-values
  (fn [db]
    (:all-values db)))

(re-frame/reg-sub
  ::items

  (fn [db]
    (:items db)))

(re-frame/reg-sub
  ::data
  (fn [db]
    (:data db)))

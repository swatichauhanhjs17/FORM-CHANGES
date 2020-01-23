
(ns trial.specs
  (:require [clojure.spec.alpha :as s] ) )


(s/def ::email (s/and string? (partial re-matches #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")))
(s/def ::identity string?)
(s/def ::form (s/keys :req-un [::email ::identity ::number ]))
(s/def ::number  (s/and (> number 999999999) (< number 10000000000)))

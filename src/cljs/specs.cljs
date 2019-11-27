
(ns specs.view
  :require [clojure.spec.alpha :as s] )
(s/valid? ::defn my-form "FORM-SUBMITTED")
(s/def ::identity string?)


(s/def ::email ::email-type)

(ns dev-dive.07-tap-let
  (:require [pez.taplet :refer [let>]]))

(comment
  ;; let-tap
  (let> [a 1
         b :keyword
         c "string"
         {name :name
          age  :age
          :as  d} {:name    "Harry"
                   :surname "Porter"
                   :age     20}]
    [a b c d name age]))

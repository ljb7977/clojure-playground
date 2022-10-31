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

(defn tap>> [x]
  (tap> x)
  x)

(comment
  ;; convenient tap> in threading macro
  (let [t [{:subject :math
            :score 100
            :grade "A+"}
           {:subject :english
            :score 90
            :grade "A"}
           {:subject :science
            :score 80
            :grade "B"}]]
    (->> t
         (filter #(> (:score %) 80))
         (tap>>)
         (map :score))))

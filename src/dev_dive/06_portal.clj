(ns dev-dive.06-portal)

(defn ->tap [x]
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
         (->tap)
         (map :score))))

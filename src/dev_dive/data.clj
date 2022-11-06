(ns dev-dive.data)

(def test-results [{:subject :math, :score 100, :grade "A+", :name "철수"}
                   {:subject :english, :score 90, :grade "A", :name "철수"}
                   {:subject :science, :score 80, :grade "B", :name "철수"}
                   {:subject :math, :score 75, :grade "B", :name "영희"}
                   {:subject :english, :score 100, :grade "A+", :name "영희"}
                   {:subject :science, :score 80, :grade "B", :name "영희"}
                   {:subject :math, :score 50, :grade "C+", :name "짱구"}
                   {:subject :english, :score 40, :grade "C", :name "짱구"}
                   {:subject :science, :score 0, :grade "F", :name "짱구"}])

(def names ["짱구" "철수" "유리" "맹구" "훈이" "흰둥이"])

(defn score->grade [score]
  (cond
    (>= score 95) "A+"
    (>= score 90) "A"
    (>= score 85) "B+"
    (>= score 80) "B"
    (>= score 75) "C+"
    (>= score 70) "C"
    (>= score 65) "D+"
    (>= score 60) "D"
    :else "F"))

(defn generate-test-results []
  (-> (for [name names
            subject [:math :english :science :history :korean :music]]
        (let [score (+ 50 (rand-int 51))]
          {:name name
           :subject subject
           :score score
           :grade (score->grade score)}))
      shuffle))

(comment
  (tap> (generate-test-results)))

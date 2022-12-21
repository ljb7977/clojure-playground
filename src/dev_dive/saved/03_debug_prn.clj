(ns dev-dive.03-debug-prn)

(def test-results [{:subject :math, :score 100, :grade "A+", :name "철수"}
                   {:subject :english, :score 90, :grade "A", :name "철수"}
                   {:subject :science, :score 80, :grade "B", :name "철수"}
                   {:subject :math, :score 75, :grade "B", :name "영희"}
                   {:subject :english, :score 100, :grade "A+", :name "영희"}
                   {:subject :science, :score 80, :grade "B", :name "영희"}
                   {:subject :math, :score 50, :grade "C+", :name "짱구"}
                   {:subject :english, :score 40, :grade "C", :name "짱구"}
                   {:subject :science, :score 0, :grade "F", :name "짱구"}])

(comment
  ;; 그냥 prn
  (->> test-results
       (filter (fn [result] (> (:score result) 60)))
       (map :score)
       prn)

  (set! *print-level* 2)
  (set! *print-length* 3)

  ;; prn in let bindings
  (let [results-over-60 (filter (fn [result] (> (:score result) 60)) test-results)
        _ (prn results-over-60)
        processed-data (process-data data)
        _ (prn processed-data)]
    processed-data)

  ;; doto prn
  (let [data (doto (fetch-data db) prn)])

  ;; doto prn in threading macro
  (-> m
      (transform)
      (transform-2)
      (doto prn)))

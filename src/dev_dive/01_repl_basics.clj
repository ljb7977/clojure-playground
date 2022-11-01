(ns dev-dive.01-repl-basics
  (:require [dev-dive.data :refer [test-results]]))

(defn student-names-over-score-80 [test-results]
  (->> test-results
       (filter #(> (:score %) 80))
       (map :name)
       (set)))

(comment

  test-results

  (->> test-results
       (filter #(> (:score %) 80))
       (map :name)
       (set))

  (student-names-over-score-80 test-results)
  ,)

(ns dev-dive.07-tap-let
  (:require [dev-dive.data :refer [test-results]]
            [pez.taplet :refer [let>]]))

(defn filter-students [test-results]
  ;; let> 매크로 사용
  (let> [filtered (filter #(> (:score %) 80) test-results)
         names (map :name filtered)
         name-set (set names)]
    name-set))

(comment
  ;; 1. 그냥 실행
  (filter-students test-results)

  ;; 2. let> 매크로 사용

  ,)

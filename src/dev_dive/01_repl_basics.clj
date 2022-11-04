(ns dev-dive.01-repl-basics
  (:require [dev-dive.data :refer [test-results]]))

(defn filter-students [test-results]
  (->> test-results
       (filter #(> (:score %) 80))
       (map :name)
       (set)))

(comment
  ;; 0. 데이터 확인
  test-results
  ;; 1. 점수가 80점 이상인 학생들의 이름 구하기
  (->> test-results
       (filter #(> (:score %) 80))
       (map :name)
       (set))

  ;; 2. 함수화
  ,)

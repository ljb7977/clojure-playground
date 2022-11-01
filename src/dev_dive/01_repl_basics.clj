(ns dev-dive.01-repl-basics
  (:require [dev-dive.data :refer [test-results]]))

(comment
  ;; 값 확인
  test-results

  ;; 성적이 85점 이상인 학생만 고르기
  (->> test-results
       (filter #(> (:score %) 85))
       (map :name)
       set)
  ,)

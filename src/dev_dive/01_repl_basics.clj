(ns dev-dive.01-repl-basics
  (:require [dev-dive.data :refer [test-results generate-test-results]]))

(comment
  ;; 0. 데이터 확인
  test-results

  ;; 1. 점수가 80점 이상인 학생들의 이름 구하기
  (->> test-results)

  ;; 2. 함수로 분리하기


  ;; 3. 더 많은 데이터에 대해 적용
  (generate-test-results)
  ,)

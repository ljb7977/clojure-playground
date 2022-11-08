(ns dev-dive.a-repl-basics
  (:require [dev-dive.data :refer [example-products products-from-db]]))

(defn top-price-increased-products [products])

(comment
  ;; 0. 데이터 확인
  example-products

  ;; 1. 시세의 변동폭이 10% 이상인 과일/채소들을, 변동폭이 높은 순서대로 이름을 구하기

  ;; 2. 함수로 분리하기
  (top-price-increased-products example-products)

  ;; 3. 더 많은 데이터에 대해 적용
  (def more-products (products-from-db))

  (top-price-increased-products more-products)
  ,)

(ns dev-dive.01-repl-basics
  (:require [dev-dive.data :refer [example-products generate-products]]))

(defn top-price-increased-products [products]
  (->> products
       (map (fn [{:keys [prev-price price] :as product}] (assoc product :price-diff (- price prev-price))))
       (map (fn [{:keys [prev-price price-diff] :as product}] (assoc product :price-diff-rate (* (/ price-diff prev-price) 1.0))))
       (filter (fn [{:keys [price-diff-rate]}] (> price-diff-rate 0.1)))
       (sort-by :price-diff-rate)))
       ;(map :name)))

(comment
  ;; 0. 데이터 확인
  example-products

  ;; 1. 시세의 변동폭이 20% 이상인 과일/채소들을, 변동폭이 높은 순서대로 이름을 구하기
  (->> example-products
       (map (fn [{:keys [prev-price price] :as product}] (assoc product :price-diff (- price prev-price))))
       (map (fn [{:keys [prev-price price-diff] :as product}] (assoc product :price-diff-rate (* (/ price-diff prev-price) 1.0))))
       (filter (fn [{:keys [price-diff-rate]}] (> price-diff-rate 0.1)))
       (sort-by :price-diff-rate)
       (map :name)
       ,)

  ;; 2. 함수로 분리하기

  ;; 3. 더 많은 데이터에 대해 적용
  (def more-products (generate-products))

  (top-price-increased-products more-products)
  ,)

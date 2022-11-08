(ns dev-dive.b-portal
  (:require [dev-dive.data :refer [example-products products-from-db]]))

;; ->tap 정의
(defn ->tap [x]
  (tap> x)
  x)

;; 3. 사이사이에 ->tap 사용
(defn top-price-increased-products [products]
  (->> products
       (map (fn [{:keys [prev-price price] :as product}]
              (assoc product :price-diff (- price prev-price))))
       (map (fn [{:keys [prev-price price-diff] :as product}]
              (assoc product :price-diff-rate (* (/ prev-price price-diff) 100.0))))
       (filter (fn [{:keys [price-diff-rate]}] (> price-diff-rate 10)))
       (sort-by :price-diff-rate >)
       (map :name)))

(comment
  ;; 1. 기본 사용법
  (tap> "Hello world")
  (tap> [1 2 3 4 5])
  (tap> {:name "Jubeen"
         :surname "Lee"
         :job "Programmer"
         :language "Clojure"})
  (tap> example-products)

  '("상추" "바나나" "사과" "양배추")

  ;; 2. 스레딩 매크로
  ;(->> example-products
  ;     (map (fn [{:keys [prev-price price] :as product}] (assoc product :price-diff (- price prev-price))))
  ;     (map (fn [{:keys [prev-price price-diff] :as product}] (assoc product :price-diff-rate (* (/ price-diff prev-price) 1.0))))
  ;     (filter (fn [{:keys [price-diff-rate]}] (> price-diff-rate 0.1)))
  ;     (sort-by :price-diff-rate >)
  ;     (map :name)
  ;     ,)

  (top-price-increased-products example-products)

  ;; 3. 많은 양의 데이터
  (def more-products (products-from-db))
  (tap> more-products)

  ;; 3
  (top-price-increased-products more-products))

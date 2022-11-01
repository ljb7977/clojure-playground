(ns dev-dive.06-portal
  (:require [dev-dive.data :refer [test-results
                                   generate-test-results]]))

(defn ->tap [x]
  (tap> x)
  x)

(defn filter-students [test-results]
  (->> test-results
       (->tap)
       (filter #(> (:score %) 80))
       (->tap)
       (map :name)
       (->tap)
       (set)))

(comment
  (require '[portal.api :as p])
  (add-tap #'p/submit)
  (def p (p/open {:launcher :intellij}))

  ;; basic usages
  (tap> "Hello World")

  (tap> [1 2 3 4 5])

  (tap> {:name "Jubeen"
         :age 26})

  ;; convenient tap in threading macro
  (->> test-results
       (filter #(< (:score %) 60))
       ;; 여기서 filter의 결과값을 알고 싶다면..?
       (->tap)
       (map :score))

  (filter-students test-results)

  (tap> (generate-test-results)))

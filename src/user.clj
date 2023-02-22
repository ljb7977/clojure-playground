(ns user)

(defn ->tap [x]
  (tap> x)
  x)

(require '[java-time.api :as jt])
(comment
  (jt/local-date 2023 1 20))

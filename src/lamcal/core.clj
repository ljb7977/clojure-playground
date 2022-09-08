(ns lamcal.core
  (:require [uncomplicate.fluokitten.core :refer [curry]]))

; == 참고용 ==
(defn curried-fn [func args-len]
  (fn [& args]
    (let [remaining (- args-len (count args))]
      (if (zero? remaining)
        (apply func args)
        (curried-fn (apply partial func args) remaining)))))
; ===========

(defmacro defn' [fname args & body]
  `(let [fun# (fn ~args (do ~@body))]
     (def ~fname (curry fun#))))

(defn' zero [f x]
  x)

(defn' one [f x]
       (f x))

(defn' two [f x]
  (f (f x)))

(defn' three [f x]
  (f (f (f x))))

; and so on...
;(defn' church-n' [n f x]
;  (if (= n 0)
;    x
;    (f (church-n' (dec n) f x))))

;(defn' church-n
;  ;"f를 x에 n번 적용하는 함수를 반환합니다"
;  [n]
;  (if (= n 0)
;    (fn [f x] x)
;    (fn [f x] (f ((church-n (dec n)) f x)))))

(defn' church-n-iterate [n f x]
  (->> (iterate f x)
       (drop n)
       (first)))
;; iterate와 똑같음

;; successor function
;; = inc
(defn' succ
  ;"n: church numeral n. x에 f를 n번 적용하라는 함수"
  [n f x]
  (f (n f x)))

(defn' add [m n]
  (n succ m))

(defn' mul [m n f]
  (m (n f)))

(comment
  (zero inc 0)

  (one inc 0)

  (three inc 0)

  (one inc 1)
  ((add (curry two) (curry three)) inc 0)
  ((two succ three) inc 0)

  (((mul two three) inc) 0)


  (partial succ three)
  (succ (partial succ three) inc 0)

  ((partial succ one) inc 0)

  ((church-n 4) inc 0))
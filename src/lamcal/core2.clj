(ns lamcal.core2)


(def zero
  (fn [f] (fn [x] x)))

(def one
  (fn [f] (fn [x] (f x))))

(def two
  (fn [f] (fn [x] (f (f x)))))

(def succ
  (fn [n] (fn [f] (fn [x] (f ((n f) x))))))

(comment
  ((zero inc) 0)

  (((succ two) inc) 0)

  ((two inc) 0))
(ns y2021.day1
  (:require util))

(def input-val
  (util/read-lines "y2021/day1.txt"))

; ------- Part 1 -------
(->> input-val
     (map #(Integer/parseInt %))
     (reduce (fn [{last :last acc :acc} val]
               {:acc (conj acc (- val last))
                :last val})
             {:acc [] :last 0})
     :acc
     rest
     (filter pos-int?)
     count)

; ------- Part 2 --------
(->> input-val
     (map #(Integer/parseInt %))
     (partition 3 1)
     (map #(reduce + %))
     (reduce (fn [{last :last acc :acc} val]
               {:acc (conj acc (- val last))
                :last val})
             {:acc [] :last 0})
     :acc
     rest
     (filter pos-int?)
     count)

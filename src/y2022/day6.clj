(ns y2022.day6)

(def input (slurp "src/y2022/day6.txt"))

(defn all-different? [s]
  (let [len (count s)]
    (= len (-> s set count))))

(defn solve [input n]
  (let [len (- (count input) n)]
    (->> (range len)
         (map #(subs input % (+ % n)))
         (take-while (complement all-different?))
         count
         (+ n))))

(comment
  ;; Part 1
  (solve input 4)
  := 1566

  (solve input 14)
  := 2265
  :rcf)

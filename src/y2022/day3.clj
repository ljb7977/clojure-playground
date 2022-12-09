(ns y2022.day3)

(def input (slurp "src/y2022/day3.txt"))

(defn split-half [s]
  (let [len (count s)]
    [(subs s 0 (/ len 2)) (subs s (/ len 2) len)]))

(defn char->priority [c]
  (let [ascii-code (int c)]
    (cond
      (<= 97 ascii-code 122) (+ (- ascii-code 97) 1)  ;; 소문자
      (<= 65 ascii-code 90)  (+ (- ascii-code 65) 27) ;; 대문자
      ,)))

(comment
  (->> (clojure.string/split-lines input)
       (map split-half)
       (map (fn [[f s]] (clojure.set/intersection (set f) (set s))))
       (map first)
       (map char->priority)
       (apply +)))

(ns y2023.day1)

(def sample-input 
  "1abc2\npqr3stu8vwx\na1b2c3d4e5f\ntreb7uchet")

(def sample-input-2 
  "two1nine\neightwothree\nabcone2threexyz\nxtwone3four\n4nineeightseven2\nzoneight234\n7pqrstsixteen")

(defn digits->int [[x y]]
  (+ (* 10 (- (int x) (int \0))) (- (int y) (int \0))))

(def input (slurp "src/y2023/day1.txt"))


(defn replace-digit [s]
  (-> s
      (clojure.string/replace "one" "o1e")
      (clojure.string/replace "two" "t2o")
      (clojure.string/replace "three" "t3e")
      (clojure.string/replace "four" "f4r")
      (clojure.string/replace "five" "f5e")
      (clojure.string/replace "six" "s6x")
      (clojure.string/replace "seven" "s7n")
      (clojure.string/replace "eight" "e8t")
      (clojure.string/replace "nine" "n9e")))

(comment
  ; Part 1
  (->> (clojure.string/split-lines input)
       (map #(filter (fn [c] (Character/isDigit c)) %))
       (map (fn [digits] [(first digits) (last digits)]))
       (map digits->int)
       (apply +))
  
  ; Part 2
  (->> (clojure.string/split-lines input)
       (map replace-digit)
       (map #(filter (fn [c] (Character/isDigit c)) %))
       (map (fn [digits] [(first digits) (last digits)]))
       (map digits->int)
       (apply +)))

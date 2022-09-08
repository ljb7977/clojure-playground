(ns y2021.day4
  (:require
    [clojure.string :refer [split]]
    [util :refer [read-lines]]))

(def input-lines
  (read-lines "y2021/day4.txt"))

(defn ->prn [v]
  (prn v)
  v)

(defn parse-board [lines]
  (mapv (fn [line] (->> (split line #" ")
                        (filter (complement empty?))
                        (mapv #(Integer/parseInt %))))
        lines))

(defn parse-boards [lines]
  (loop [lines lines
         result []]
    (if (empty? lines)
      result
      (let [board-lines (rest lines)
            board-lines' (take 5 board-lines)
            next-lines (drop 5 board-lines)]
        (recur next-lines (conj result (parse-board board-lines')))))))

(defn parse-input [lines]
  (let [first-line (split (first lines) #",")
        drawing-numbers (map #(Integer/parseInt %) first-line)
        rest-lines (rest lines)]
    (parse-boards rest-lines)))

(comment
  (parse-input input-lines)

  (parse-board ["31 23 52 26  8"
                "27 89 37 80 46"
                "97 19 63 34 79"
                "13 59 45 12 73"
                "42 25 22  6 39"]))

(ns y2022.day5
  (:require [user :refer [->tap]]))

(def input (slurp "src/y2022/day5.txt"))

(defn pick-char-at-row-col [state-lines row col]
  (let [rows (dec (count state-lines))
        x (inc (* (dec col) 4))
        y (- (dec rows) (dec row))]
    (-> state-lines
        (nth y)
        (nth x))))

(defn parse-initial-state [initial-state]
  (let [initial-state-lines (clojure.string/split initial-state #"\n")
        rows (dec (count initial-state-lines))
        columns (-> (first initial-state-lines)
                    count
                    inc
                    (/ 4))]
    (->> (for [column (range 1 (inc columns))]
           (->> (range 1 (inc rows))
                (map #(pick-char-at-row-col initial-state-lines % column))
                (remove (fn [s] (= \space s)))
                (into [])))
         (into [nil]))))

(defn parse-move [move]
  (let [[_ n from to] (re-matches #"move (\d+) from (\d+) to (\d+)" move)] 
    (map parse-long [n from to])))

(defn process-move [{:keys [stacks from to]}]
  (let [from-column (nth stacks from)
        top-elem (peek from-column)
        from-column' (pop from-column)

        to-column (nth stacks to)
        to-column' (conj to-column top-elem)]
    {:stacks (-> stacks
                 (assoc from from-column')
                 (assoc to to-column'))
     :from from
     :to to}))

(defn process-moves [stacks [n from to]]
  (->> {:stacks stacks
        :from from
        :to to}
       (iterate process-move)
       (drop n)
       first
       :stacks))

(defn process-moves-multiple [stacks [n from to]]
  (let [from-column (nth stacks from)
        len (count from-column)
        [from-column' top-elems] (->> from-column
                                      (split-at (- len n))
                                      (map vec))
        to-column (nth stacks to)
        to-column' (vec (concat to-column top-elems))]
    (-> stacks
        (assoc from from-column')
        (assoc to to-column'))))

(defn get-top-crates [stacks]
  (->> (rest stacks)
       (map peek)))

(defn parse-input [input]
  (let [[init-state-str moves] (clojure.string/split input #"\n\n")
        stacks (parse-initial-state init-state-str)
        moves (->> (clojure.string/split-lines moves)
                   (map parse-move))]
    [stacks moves]))

(comment
  ;; Part 1
  (let [[stacks moves] (parse-input input)]
    (->> moves
         (reduce process-moves stacks)
         get-top-crates
         (apply str)))
  := "TDCHVHJTG"

  (let [[stacks moves] (parse-input input)]
    (->> moves
         (reduce process-moves-multiple stacks)
         get-top-crates
         (apply str)))
  := "NGCMPJLHV"

  :rcf)

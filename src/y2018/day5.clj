(ns y2018.day5
  (:require util))

(def input-val
  (first (util/read-lines "y2018/day5.txt")))

(def example-input "dabAcCaCBAcCcaDA")

(defn abs [n]
  (max n (- n)))

(defn same-letter-but-opposite-capitalization?
  "두 문자가 서로 같으면서 대소문자 여부만 다른지를 체크합니다.

  Input: a b
  Output: false

  Input: a A
  Output: true
  "
  [f g]
  (= (abs (- (int f) (int g))) 32))

(defn remove-matching-pairs
  "문자열 전체를 한번 스캔하면서 지울 수 있는 문자를 최대한 지웁니다.
  스택에 문자를 넣고, 그 다음 문자가 왔을 때 앞의 문자와 대소문자만 다르면 스택에서 제거하는 것을 반복합니다.
  Input: dabAcCaCBAcCcaDA
  Output: [d a b C B A c a D A]
  "
  [s]
  (reduce (fn [stack c]
            (let [last-char (peek stack)]
              (cond
                (nil? last-char) [c]
                (same-letter-but-opposite-capitalization? last-char c) (pop stack)
                :else (conj stack c))))
          []
          s))

;(remove-matching-pairs example-input)

(defn remove-until-no-change [s]
  (loop [prev s]
    (let [removed (remove-matching-pairs prev)]
      (if (= removed prev)
        removed
        (recur removed)))))

; reduce 버전. loop-recur보다 나은가...? 아니면 더 깔쌈한 방법이 있을지...?
(defn remove-until-no-change-2 [s]
  (reduce (fn [prev _]
            (let [removed (remove-matching-pairs prev)]
              (if (= removed prev) (reduced prev) removed))) s (repeat nil)))

(comment
  ; ---- Part 1
  (->> input-val
       remove-until-no-change
       count))

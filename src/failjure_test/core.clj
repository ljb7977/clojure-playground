(ns failjure-test.core
  (:require [failjure.core :as f]))


; 1: try catch
(comment
  (def exception (Exception. "HAHA!"))
  (throw exception)
  (try
    (/ 1 0)
    (catch Exception e (str "Exception caught: " e)))
  (try
    (throw (Exception. "exception"))
    (catch Exception e (str "Exception caught: " e))))


; 2: failjure
; f/fail
(f/fail "failure")
(f/fail "Failed. Cause: %s" "reason")                       ; format 형식 지원
(f/fail {:message "message" :data {:a 1}})

(f/failed? (f/fail "failure"))                              ; true
(f/failed? exception)                                       ; true
(f/failed? nil)                                             ; false

(f/message exception)
(f/message (f/fail "failure"))

; attempt: 값과 함수를 받아서, 값이 failure이면 함수에 그 값을 넘겨서 결과를 반환한다.
; 값이 failure가 아니면 그냥 값을 반환한다.
(defn handle-error [e] (str "handle-error: " (f/message e)))
(f/attempt handle-error "Success")
(f/attempt handle-error (f/fail "Failure"))
(f/attempt handle-error exception)

; attempt-all: let 바인딩에서 실패 처리를 변하게 해 줌
(f/attempt-all [x "Ok"
                y "Ok2"]
  x)
(f/attempt-all [y (f/fail "Fail")                           ; short-circuiting
                x "Ok"]
  x)
; when-failed: attempt-all과 항상 같이 써야 함
(f/attempt-all [y (f/fail "Fail")
                x "Ok"]
  x
  (f/when-failed [e]                                        ; 에러 케이스 핸들링
    (f/message e)))

; ok threading macro: ok-> ok->>
(defn validate-non-blank [data field]
  (if (empty? (get data field))
    (f/fail "Value required for %s" field)
    data))

(let [data {:username nil
            :password "password"}
      result (f/ok->
               data
               (validate-non-blank :username)
               (validate-non-blank :password)
               (prn))]
  (if (f/failed? result)
    (f/message result)                                      ; 실패 케이스 처리
    result))
; as-ok->
(f/as-ok-> "k" $
           (str $ "!")
           (f/try* (Integer/parseInt $))
           (str "O" $))


; try* : 던져진 exception을 잡아서 리턴하는 매크로
(-> (f/try*
      (throw (Exception. "excpetion")))
    (f/failed?))
;try-all : try*의 let binding 버전
(f/try-all [x (/ 1 0)
            y (* 2 3)]
  y)

; (if|when)-let-(ok?|failed?)
(f/if-let-failed? [x (f/fail "failure")]
  (println (str "Error: " (f/message x)))
  x)

; assert-with : failjure를 안 쓰는 함수를 failjure를 쓰도록 해주는 어댑터
; return value를 predicate에 넣어서 성공/실패로 바꿔 준다.
(f/attempt-all
  [x (f/assert-with some? (some-fn) "some-fn failed!")
   y (f/assert-with integer? (some-integer-returning-fn) "Not an integer.")]
  (handle-success x)
  (f/when-failed [e] (handle-failure e)))

; Total example
(defn validate-email [email]
  (if (re-matches #".+@.+\..+" email)
    email
    (f/fail "Please enter a valid email address (got %s)" email)))

(defn validate-not-empty [s]
  (if (empty? s)
    (f/fail "Please enter a value")
    s))


;; Use attempt-all to handle failures
(defn validate-data [data]
  (f/attempt-all [email (validate-email (:email data))
                  username (validate-not-empty (:username data))
                  id (f/try* (Integer/parseInt (:id data)))]
    {:email    email
     :username username}
    (f/when-failed [e]
      (clojure.pprint/pprint (f/message e)))))


(comment
  (f/fail "failure")
  (Exception. "exception")

  (f/failed? (f/try*
               (throw (Exception. "message"))))
  (f/failed? (Exception. "messsage"))                       ; 이거와 같음
  (Exception. "message")

  (f/failed? (throw (Exception. "message")))                ; 이건 다름. exception이 throw된 상태여서 잡을 수가 없음.

  (let [result (f/try*
                 (f/ok-> {}
                         (assoc :a 1)
                         (bomb)
                         (assoc :b 1)))]
    (if (f/failed? result)
      (println (str "failed: " (f/message result)))
      (println "succeed"))))

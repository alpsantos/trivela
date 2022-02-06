(ns trivela.components.worker
  (:require [com.stuartsierra.component :as component]
            [trivela.protocols.worker :as protocols.worker]))


(defn task-fn [this]
         (println "Executing task-fn")
         (Thread/sleep 300))

(defn make-task2 [{:keys [flag] :as this}]
  (future
    (while @flag        ;; ...try/catch
      (task-fn this))))



(defrecord Worker
  [options flag task task2]

  component/Lifecycle
  (start [this]
    (let [flag (atom true)
          this (assoc this :flag flag)
          task (task2 this)]
      (println this)
      (assoc this :task task)))

  (stop [this]
    (reset! flag false)
    (while (not (realized? task))
      ;(log/info "Waiting for the task to complete")
      (println "Waiting for the task to complete")
      (Thread/sleep 300))
    (assoc this :flag nil :task nil))

  protocols.worker/IWorker
  (make-task [this]
    (future
      (while @flag        ;; ...try/catch
        (task-fn this))))
  )

(def make-worker (map->Worker {:task2 make-task2}))

(def started (component/start make-worker))
(type started)
;(component/stop started)
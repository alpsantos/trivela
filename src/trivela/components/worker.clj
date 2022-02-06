(ns trivela.components.worker
  (:require [com.stuartsierra.component :as component]))

(defn task-fn [this]
         (println "Executing task-fn")
         (Thread/sleep 300))

(defn make-task [{:keys [flag] :as this}]
  (future
    (while @flag        ;; ...try/catch
      (task-fn this))))

(defrecord Worker
  [options flag task]

  component/Lifecycle
  (start [this]
    (let [flag (atom true)
          this (assoc this :flag flag)
          task-to-run (:task-to-run options)
          task (task-to-run this)]
      (println this)
      (assoc this :task task)))

  (stop [this]
    (reset! flag false)
    (while (not (realized? task))
      (println "Waiting for the task to complete")
      (Thread/sleep 300))
    (assoc this :flag nil :task nil)))

(def make-worker (map->Worker {:options {:task-to-run make-task} }))

(def started (component/start make-worker))
(type started)
;(component/stop started)
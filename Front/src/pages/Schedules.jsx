import { useEffect, useState } from "react"
import api from "../services/api"

function Schedules() {

  const [medicos, setMedicos] = useState([])

  const [doctorFiltro, setDoctorFiltro] = useState("")

  const [schedules, setSchedules] = useState([])

  const [mostrarFormulario, setMostrarFormulario] = useState(false)

  const [scheduleEditando, setScheduleEditando] = useState(null)

  const [novoSchedule, setNovoSchedule] = useState({
    dayOfWeek: "MONDAY",
    startTime: "",
    endTime: "",
    breakStart: "",
    breakEnd: ""
  })

  const diasSemana = [
    { label: "Segunda", value: "MONDAY" },
    { label: "Terça", value: "TUESDAY" },
    { label: "Quarta", value: "WEDNESDAY" },
    { label: "Quinta", value: "THURSDAY" },
    { label: "Sexta", value: "FRIDAY" },
    { label: "Sábado", value: "SATURDAY" },
    { label: "Domingo", value: "SUNDAY" }
  ]

  useEffect(() => {

    carregarMedicos()

  }, [])

  useEffect(() => {

    if (doctorFiltro) {

      carregarSchedules()

    }

  }, [doctorFiltro])

  function carregarMedicos() {

    api.get("/doctors")

      .then(response => {

        setMedicos(response.data.content)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function carregarSchedules() {

    api.get(`/schedules/by-doctor?doctorId=${doctorFiltro}`)

      .then(response => {

        setSchedules(response.data)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function salvarSchedule() {

    const body = {
      ...novoSchedule,
      doctorId: doctorFiltro
    }

    if (scheduleEditando) {

      api.put(
        `/schedules/${scheduleEditando.id}`,
        body
      )

        .then(() => {

          carregarSchedules()

          limparFormulario()

        })

        .catch(error => {

          alert(
            error.response?.data?.message
            || "Erro ao atualizar agenda"
          )

        })

    } else {

      api.post("/schedules", body)

        .then(() => {

          carregarSchedules()

          limparFormulario()

        })

        .catch(error => {

          alert(
            error.response?.data?.message
            || "Erro ao salvar agenda"
          )

        })
    }
  }

  function editarSchedule(schedule) {

    setScheduleEditando(schedule)

    setNovoSchedule({
      dayOfWeek: schedule.dayOfWeek,
      startTime: schedule.startTime,
      endTime: schedule.endTime,
      breakStart: schedule.breakStart,
      breakEnd: schedule.breakEnd
    })

    setMostrarFormulario(true)
  }

  function excluirSchedule(id) {

    api.delete(`/schedules/${id}`)

      .then(() => {

        carregarSchedules()

      })

      .catch(error => {

        console.log(error)

      })
  }

  function limparFormulario() {

    setNovoSchedule({
      dayOfWeek: "MONDAY",
      startTime: "",
      endTime: "",
      breakStart: "",
      breakEnd: ""
    })

    setScheduleEditando(null)

    setMostrarFormulario(false)
  }

  function traduzirDia(dia) {

    const dias = {
      MONDAY: "Segunda",
      TUESDAY: "Terça",
      WEDNESDAY: "Quarta",
      THURSDAY: "Quinta",
      FRIDAY: "Sexta",
      SATURDAY: "Sábado",
      SUNDAY: "Domingo"
    }

    return dias[dia]
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-8">

        <h1 className="text-3xl font-bold">
          Gerenciar Agenda
        </h1>

      </div>

      <div className="bg-white p-6 rounded-2xl shadow mb-8">

        <select
          value={doctorFiltro}
          onChange={(e) =>
            setDoctorFiltro(e.target.value)
          }
          className="border p-4 rounded-xl w-full"
        >

          <option value="">
            Selecione um Médico
          </option>

          {
            medicos.map(medico => (

              <option
                key={medico.id}
                value={medico.id}
              >

                {medico.name}

              </option>
            ))
          }

        </select>

      </div>

      {
        !doctorFiltro && (

          <div className="bg-white rounded-2xl shadow p-12 text-center text-gray-500 text-lg">

            Selecione um médico para visualizar e gerenciar agendas

          </div>
        )
      }

      {
        doctorFiltro && (

          <>

            <div className="flex justify-end mb-6">

              <button
                onClick={() => setMostrarFormulario(true)}
                className="bg-blue-600 text-white px-5 py-3 rounded-xl hover:bg-blue-700"
              >

                Nova Agenda

              </button>

            </div>

            {
              mostrarFormulario && (

                <div className="bg-white p-6 rounded-2xl shadow mb-8">

                  <h2 className="text-2xl font-bold mb-6">

                    {
                      scheduleEditando
                        ? "Editar Agenda"
                        : "Nova Agenda"
                    }

                  </h2>

                  <div className="grid grid-cols-2 gap-4">

                    <select
                      value={novoSchedule.dayOfWeek}
                      onChange={(e) =>
                        setNovoSchedule({
                          ...novoSchedule,
                          dayOfWeek: e.target.value
                        })
                      }
                      className="border p-3 rounded-xl"
                    >

                      {
                        diasSemana.map(dia => (

                          <option
                            key={dia.value}
                            value={dia.value}
                          >

                            {dia.label}

                          </option>
                        ))
                      }

                    </select>

                  </div>

                  <div className="grid grid-cols-2 gap-6 mt-6">

                    <div>

                      <label className="block mb-2 font-semibold">
                        Horário de Entrada
                      </label>

                      <input
                        type="time"
                        value={novoSchedule.startTime}
                        onChange={(e) =>
                          setNovoSchedule({
                            ...novoSchedule,
                            startTime: e.target.value
                          })
                        }
                        className="border p-3 rounded-xl w-full"
                      />

                    </div>

                    <div>

                      <label className="block mb-2 font-semibold">
                        Horário de Saída
                      </label>

                      <input
                        type="time"
                        value={novoSchedule.endTime}
                        onChange={(e) =>
                          setNovoSchedule({
                            ...novoSchedule,
                            endTime: e.target.value
                          })
                        }
                        className="border p-3 rounded-xl w-full"
                      />

                    </div>

                    <div>

                      <label className="block mb-2 font-semibold">
                        Início do Almoço
                      </label>

                      <input
                        type="time"
                        value={novoSchedule.breakStart}
                        onChange={(e) =>
                          setNovoSchedule({
                            ...novoSchedule,
                            breakStart: e.target.value
                          })
                        }
                        className="border p-3 rounded-xl w-full"
                      />

                    </div>

                    <div>

                      <label className="block mb-2 font-semibold">
                        Fim do Almoço
                      </label>

                      <input
                        type="time"
                        value={novoSchedule.breakEnd}
                        onChange={(e) =>
                          setNovoSchedule({
                            ...novoSchedule,
                            breakEnd: e.target.value
                          })
                        }
                        className="border p-3 rounded-xl w-full"
                      />

                    </div>

                  </div>

                  <div className="flex gap-3 mt-6">

                    <button
                      onClick={salvarSchedule}
                      className="bg-green-600 text-white px-5 py-3 rounded-xl"
                    >

                      Salvar

                    </button>

                    <button
                      onClick={limparFormulario}
                      className="bg-gray-500 text-white px-5 py-3 rounded-xl"
                    >

                      Cancelar

                    </button>

                  </div>

                </div>
              )
            }

            <div className="grid gap-4">

              {
                schedules.map(schedule => (

                  <div
                    key={schedule.id}
                    className="bg-white p-6 rounded-2xl shadow"
                  >

                    <div className="flex justify-between items-center">

                      <div>

                        <h2 className="text-2xl font-bold">

                          {traduzirDia(schedule.dayOfWeek)}

                        </h2>

                        <div className="text-gray-600 mt-2">

                          {schedule.startTime} às {schedule.endTime}

                        </div>

                        <div className="text-gray-500 mt-1">

                          Almoço:
                          {" "}
                          {schedule.breakStart}
                          {" às "}
                          {schedule.breakEnd}

                        </div>

                      </div>

                      <div className="flex gap-3">

                        <button
                          onClick={() =>
                            editarSchedule(schedule)
                          }
                          className="bg-yellow-400 px-4 py-2 rounded-xl"
                        >

                          Editar

                        </button>

                        <button
                          onClick={() =>
                            excluirSchedule(schedule.id)
                          }
                          className="bg-red-500 text-white px-4 py-2 rounded-xl"
                        >

                          Excluir

                        </button>

                      </div>

                    </div>

                  </div>
                ))
              }

            </div>

          </>
        )
      }

    </div>
  )
}

export default Schedules
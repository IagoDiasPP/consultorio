import { useEffect, useState } from "react"
import api from "../services/api"

function Consultas() {

  const [consultas, setConsultas] = useState([])

  const [pacientes, setPacientes] = useState([])

  const [especialidades, setEspecialidades] = useState([])

  const [mostrarFormulario, setMostrarFormulario] = useState(false)

  const [consultaSelecionada, setConsultaSelecionada] = useState(null)

  const [mensagemSistema, setMensagemSistema] = useState("")

  const [statusFiltro, setStatusFiltro] = useState("ALL")

  const [novaConsulta, setNovaConsulta] = useState({
    patientId: "",
    specialtyId: ""
  })

  const [consultaExcluir, setConsultaExcluir] = useState(null)

  useEffect(() => {

    carregarConsultas()

    carregarPacientes()

    carregarEspecialidades()

  }, [])

  function carregarConsultas() {

    api.get("/appointments")

      .then(response => {

        setConsultas(response.data)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function carregarPacientes() {

    api.get("/patients")

      .then(response => {

        setPacientes(response.data.content)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function carregarEspecialidades() {

    api.get("/specialties")

      .then(response => {

        setEspecialidades(response.data)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function salvarConsulta() {

    api.post("/appointments", novaConsulta)

      .then(response => {

        carregarConsultas()

        const consulta = response.data

        if (consulta.status === "CONFIRMED") {

          setMensagemSistema(
            `Consulta marcada automaticamente para ${consulta.date} às ${consulta.startTime
  ? consulta.startTime.slice(0, 5)
  : "--"} com Dr(a) ${consulta.doctor.name}`
          )

        } else {

          setMensagemSistema(
            "Paciente adicionado na fila de espera"
          )
        }

        setMostrarFormulario(false)

        setNovaConsulta({
          patientId: "",
          specialtyId: ""
        })

      })

      .catch(error => {

        console.log(error)

        alert(
          error.response?.data?.message
          || "Erro ao criar consulta"
        )

      })
  }

function excluirConsulta() {

  api.delete(
    `/appointments/${consultaExcluir.id}`
  )

    .then(() => {

      carregarConsultas()

      setConsultaExcluir(null)

    })

    .catch(error => {

      console.log(error)

      alert(
        "Erro ao excluir consulta"
      )

    })
}
  function traduzirStatus(status) {

    const nomes = {

      CONFIRMED: "Confirmada",

      WAITING: "Fila de Espera",

      PENDING: "Pendente"

    }

    return nomes[status]
  }

  function corStatus(status) {

    const cores = {

      CONFIRMED:
        "bg-green-100 text-green-700",

      WAITING:
        "bg-yellow-100 text-yellow-700",

      PENDING:
        "bg-blue-100 text-blue-700"

    }

    return cores[status]
  }

  const consultasFiltradas =

    statusFiltro === "ALL"

      ? consultas

      : consultas.filter(
          consulta =>
            consulta.status === statusFiltro
        )

  return (

    <div>

      <div className="flex justify-between items-center mb-8">

        <h1 className="text-3xl font-bold">

          Central de Consultas

        </h1>

        <button
          onClick={() =>
            setMostrarFormulario(true)
          }
          className="bg-blue-600 text-white px-5 py-3 rounded-2xl hover:bg-blue-700"
        >

          Nova Consulta

        </button>

      </div>

      {
        mensagemSistema && (

          <div className="bg-blue-100 text-blue-800 p-4 rounded-2xl mb-6 font-semibold">

            {mensagemSistema}

          </div>
        )
      }

      <div className="bg-white p-6 rounded-2xl shadow mb-8">

        <select
          value={statusFiltro}
          onChange={(e) =>
            setStatusFiltro(e.target.value)
          }
          className="border p-3 rounded-xl"
        >

          <option value="ALL">
            Todas
          </option>

          <option value="CONFIRMED">
            Confirmadas
          </option>

          <option value="WAITING">
            Fila de Espera
          </option>

          <option value="PENDING">
            Pendentes
          </option>

        </select>

      </div>

      {
        mostrarFormulario && (

          <div className="bg-white p-6 rounded-2xl shadow mb-8">

            <h2 className="text-2xl font-bold mb-6">

              Nova Consulta

            </h2>

            <div className="grid grid-cols-2 gap-4">

              <select
                value={novaConsulta.patientId}
                onChange={(e) =>
                  setNovaConsulta({
                    ...novaConsulta,
                    patientId: e.target.value
                  })
                }
                className="border p-3 rounded-xl"
              >

                <option value="">
                  Selecione o Paciente
                </option>

                {
                  pacientes.map(paciente => (

                    <option
                      key={paciente.id}
                      value={paciente.id}
                    >

                      #{paciente.id} - {paciente.name}

                    </option>
                  ))
                }

              </select>

              <select
                value={novaConsulta.specialtyId}
                onChange={(e) =>
                  setNovaConsulta({
                    ...novaConsulta,
                    specialtyId: e.target.value
                  })
                }
                className="border p-3 rounded-xl"
              >

                <option value="">
                  Selecione a Especialidade
                </option>

                {
                  especialidades.map(especialidade => (

                    <option
                      key={especialidade.id}
                      value={especialidade.id}
                    >

                      {especialidade.name}

                    </option>
                  ))
                }

              </select>

            </div>

            <div className="flex gap-3 mt-6">

              <button
                onClick={salvarConsulta}
                className="bg-green-600 text-white px-5 py-3 rounded-2xl"
              >

                Gerar Consulta

              </button>

              <button
                onClick={() => {

                  setMostrarFormulario(false)

                  setNovaConsulta({
                    patientId: "",
                    specialtyId: ""
                  })

                }}
                className="bg-gray-500 text-white px-5 py-3 rounded-2xl"
              >

                Cancelar

              </button>

            </div>

          </div>
        )
      }

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">

        {
          consultasFiltradas.map(consulta => (

            <div
              key={consulta.id}
              className="bg-white rounded-2xl shadow p-6"
            >

              <div className="flex justify-between items-start">

                <div>

                  <h2 className="text-2xl font-bold">

                    {consulta.patient.name}

                  </h2>

                  <div className="text-gray-600 mt-2">

                    {consulta.specialty.name}

                  </div>

                </div>

                <div
                  className={`px-3 py-1 rounded-full text-sm font-semibold ${corStatus(consulta.status)}`}
                >

                  {traduzirStatus(consulta.status)}

                </div>

              </div>

              <div className="mt-6 space-y-2">

                {
                  consulta.status === "CONFIRMED" && (

                    <>

                      <div>

                        <strong>
                          Médico:
                        </strong>

                        {" "}

                        {consulta.doctor.name}

                      </div>

                      <div>

                        <strong>
                          Data:
                        </strong>

                        {" "}

                        {consulta.date}

                      </div>

                      <div>

                        <strong>
                          Horário:
                        </strong>

                        {" "}

                        {consulta.date
  ? consulta.date
  : "Aguardando vaga"}

                      </div>

                    </>
                  )
                }

                {
                  consulta.status === "WAITING" && (

                    <div className="text-yellow-700 font-semibold">

                      Aguardando encaixe automático

                    </div>
                  )
                }

              </div>

              <div className="flex gap-3 mt-8">

                <button
                  onClick={() =>
                    setConsultaSelecionada(consulta)
                  }
                  className="bg-blue-600 text-white px-4 py-2 rounded-xl"
                >

                  Visualizar

                </button>

                <button
  onClick={() =>
    setConsultaExcluir(consulta)
  }
                  className="bg-red-500 text-white px-4 py-2 rounded-xl"
                >

                  Excluir

                </button>

              </div>

            </div>
          ))
        }

      </div>

      {
        consultaSelecionada && (

          <div className="fixed inset-0 bg-black/50 flex items-center justify-center">

            <div className="bg-white p-8 rounded-2xl w-[450px]">

              <h2 className="text-2xl font-bold mb-6">

                Consulta

              </h2>

              <div className="space-y-4">

                <div>

                  <strong>
                    Paciente:
                  </strong>

                  <div>
                    {consultaSelecionada.patient.name}
                  </div>

                </div>

                <div>

                  <strong>
                    Telefone:
                  </strong>

                  <div>
                    {consultaSelecionada.patient.phone}
                  </div>

                </div>

                <div>

                  <strong>
                    Especialidade:
                  </strong>

                  <div>
                    {consultaSelecionada.specialty.name}
                  </div>

                </div>

                {
                  consultaSelecionada.doctor && (

                    <div>

                      <strong>
                        Médico:
                      </strong>

                      <div>
                        {consultaSelecionada.doctor.name}
                      </div>

                    </div>
                  )
                }

                {
                  consultaSelecionada.startTime && (

                    <div>

                      <strong>
                        Horário:
                      </strong>

                      <div>
                        {consultaSelecionada.startTime.slice(0, 5)}
                      </div>

                    </div>
                  )
                }

                {
                  consultaSelecionada.date && (

                    <div>

                      <strong>
                        Data:
                      </strong>

                      <div>
                        {consultaSelecionada.date}
                      </div>

                    </div>
                  )
                }

                <div>

                  <strong>
                    Status:
                  </strong>

                  <div>
                    {
                      traduzirStatus(
                        consultaSelecionada.status
                      )
                    }
                  </div>

                </div>

              </div>

              <button
                onClick={() =>
                  setConsultaSelecionada(null)
                }
                className="bg-gray-600 text-white px-5 py-3 rounded-xl w-full mt-8"
              >

                Fechar

              </button>

            </div>

          </div>
        )
      }
      {
  consultaExcluir && (

    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">

      <div className="bg-white p-8 rounded-2xl w-[400px]">

        <h2 className="text-2xl font-bold mb-4">

          Excluir Consulta

        </h2>

        <p className="text-gray-700 mb-6">

          Deseja realmente excluir a consulta de

          <strong>
            {" "}
            {consultaExcluir.patient.name}
          </strong>

          ?

        </p>

        <div className="flex gap-3">

          <button
            onClick={excluirConsulta}
            className="bg-red-500 text-white px-4 py-3 rounded-xl flex-1"
          >

            Excluir

          </button>

          <button
            onClick={() =>
              setConsultaExcluir(null)
            }
            className="bg-gray-500 text-white px-4 py-3 rounded-xl flex-1"
          >

            Cancelar

          </button>

        </div>

      </div>

    </div>
  )
}

    </div>
  )
}

export default Consultas
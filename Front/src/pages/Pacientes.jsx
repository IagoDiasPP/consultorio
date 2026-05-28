import { useEffect, useState } from "react"
import api from "../services/api"

function Pacientes() {

  const [pacientes, setPacientes] = useState([])

  const [mostrarFormulario, setMostrarFormulario] = useState(false)

  const [pacienteEditando, setPacienteEditando] = useState(null)

  const [buscaNome, setBuscaNome] = useState("")

  const [buscaId, setBuscaId] = useState("")

  const [novoPaciente, setNovoPaciente] = useState({
    name: "",
    phone: "",
    birthDate: ""
  })

  const [pacienteExcluir, setPacienteExcluir] = useState(null)

  useEffect(() => {

    carregarPacientes()

  }, [])

  function carregarPacientes() {

    api.get("/patients")

      .then(response => {

        setPacientes(response.data.content)

      })

      .catch(error => {
        console.log(error)
      })
  }

  function salvarPaciente() {

    if (pacienteEditando) {

      api.put(
        `/patients/${pacienteEditando.id}`,
        novoPaciente
      )

        .then(() => {

          carregarPacientes()

          limparFormulario()

        })

        .catch(error => {
          console.log(error)
        })

    } else {

      api.post("/patients", novoPaciente)

        .then(() => {

          carregarPacientes()

          limparFormulario()

        })

        .catch(error => {
          console.log(error)
        })
    }
  }

  function editarPaciente(paciente) {

    setPacienteEditando(paciente)

    setNovoPaciente({
      name: paciente.name,
      phone: paciente.phone,
      birthDate: paciente.birthDate
    })

    setMostrarFormulario(true)
  }

  function excluirPaciente() {

  api.delete(
    `/patients/${pacienteExcluir.id}`
  )

    .then(() => {

      carregarPacientes()

      setPacienteExcluir(null)

    })

    .catch(error => {

      console.log(error)

      alert("Erro ao excluir paciente")

    })
}

  function buscarPorNome() {

    if (!buscaNome) {
      carregarPacientes()
      return
    }

    api.get(`/patients/search?name=${buscaNome}`)

      .then(response => {

        setPacientes(response.data)

      })

      .catch(error => {
        console.log(error)
      })
  }

  function buscarPorId() {

    if (!buscaId) {
      carregarPacientes()
      return
    }

    api.get(`/patients/${buscaId}`)

      .then(response => {

        setPacientes([response.data])

      })

      .catch(error => {

        console.log(error)

        alert("Paciente não encontrado")
      })
  }

  function limparFormulario() {

    setNovoPaciente({
      name: "",
      phone: "",
      birthDate: ""
    })

    setPacienteEditando(null)

    setMostrarFormulario(false)
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-6">

        <h1 className="text-3xl font-bold">
          Pacientes
        </h1>

        <button
          onClick={() => {

            limparFormulario()

            setMostrarFormulario(true)
          }}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          Novo Paciente
        </button>

      </div>

      <div className="bg-white p-4 rounded-xl shadow mb-6">

        <div className="grid grid-cols-2 gap-4">

          <div className="flex gap-2">

            <input
              type="text"
              placeholder="Buscar por nome"
              value={buscaNome}
              onChange={(e) => setBuscaNome(e.target.value)}
              className="border p-3 rounded-lg w-full"
            />

            <button
              onClick={buscarPorNome}
              className="bg-blue-600 text-white px-4 rounded-lg"
            >
              Buscar
            </button>

          </div>

          <div className="flex gap-2">

            <input
              type="number"
              placeholder="Buscar por ID"
              value={buscaId}
              onChange={(e) => setBuscaId(e.target.value)}
              className="border p-3 rounded-lg w-full"
            />

            <button
              onClick={buscarPorId}
              className="bg-blue-600 text-white px-4 rounded-lg"
            >
              Buscar
            </button>

          </div>

        </div>

      </div>

      {
        mostrarFormulario && (

          <div className="bg-white p-6 rounded-xl shadow mb-6">

            <h2 className="text-xl font-bold mb-4">

              {
                pacienteEditando
                  ? "Editar Paciente"
                  : "Novo Paciente"
              }

            </h2>

            <div className="grid grid-cols-2 gap-4">

              <input
                type="text"
                placeholder="Nome"
                value={novoPaciente.name}
                onChange={(e) =>
                  setNovoPaciente({
                    ...novoPaciente,
                    name: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              />

              <input
                type="text"
                placeholder="Telefone"
                value={novoPaciente.phone}
                onChange={(e) =>
                  setNovoPaciente({
                    ...novoPaciente,
                    phone: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              />

              <input
                type="date"
                value={novoPaciente.birthDate}
                onChange={(e) =>
                  setNovoPaciente({
                    ...novoPaciente,
                    birthDate: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              />

            </div>

            <div className="flex gap-3 mt-4">

              <button
                onClick={salvarPaciente}
                className="bg-green-600 text-white px-4 py-2 rounded-lg"
              >
                Salvar
              </button>

              <button
                onClick={limparFormulario}
                className="bg-gray-500 text-white px-4 py-2 rounded-lg"
              >
                Cancelar
              </button>

            </div>

          </div>
        )
      }

      <div className="bg-white rounded-xl shadow overflow-hidden">

        <table className="w-full">

          <thead className="bg-gray-100">

            <tr>

              <th className="text-left p-4">
                ID
              </th>

              <th className="text-left p-4">
                Nome
              </th>

              <th className="text-left p-4">
                Telefone
              </th>

              <th className="text-left p-4">
                Data de Nascimento
              </th>

              <th className="text-left p-4">
                Ações
              </th>

            </tr>

          </thead>

          <tbody>

            {
              pacientes.map(paciente => (

                <tr
                  key={paciente.id}
                  className="border-t hover:bg-gray-50"
                >

                  <td className="p-4">
                    {paciente.id}
                  </td>

                  <td className="p-4 font-medium">
                    {paciente.name}
                  </td>

                  <td className="p-4">
                    {paciente.phone}
                  </td>

                  <td className="p-4">
                    {paciente.birthDate}
                  </td>

                  <td className="p-4">

                    <div className="flex gap-2">

                      <button
                        onClick={() => editarPaciente(paciente)}
                        className="bg-yellow-400 px-3 py-1 rounded"
                      >
                        Editar
                      </button>

                      <button
                        onClick={() => setPacienteExcluir(paciente)}
                        className="bg-red-500 text-white px-3 py-1 rounded"
                      >
                        Excluir
                      </button>

                    </div>

                  </td>

                </tr>
              ))
            }

          </tbody>

        </table>

      </div>

      {
  pacienteExcluir && (

    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">

      <div className="bg-white p-8 rounded-2xl w-[400px]">

        <h2 className="text-2xl font-bold mb-4">

          Excluir Paciente

        </h2>

        <p className="mb-6">

          Deseja realmente excluir

          <strong>
            {" "}
            {pacienteExcluir.name}
          </strong>

          ?

        </p>

        <div className="flex gap-3">

          <button
            onClick={excluirPaciente}
            className="bg-red-500 text-white px-4 py-3 rounded-xl flex-1"
          >

            Excluir

          </button>

          <button
            onClick={() =>
              setPacienteExcluir(null)
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

export default Pacientes
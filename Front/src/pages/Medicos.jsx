import { useEffect, useState } from "react"
import api from "../services/api"

function Medicos() {

  const [medicos, setMedicos] = useState([])

  const [especialidades, setEspecialidades] = useState([])

  const [mostrarFormulario, setMostrarFormulario] = useState(false)

  const [medicoEditando, setMedicoEditando] = useState(null)

  const [buscaNome, setBuscaNome] = useState("")

  const [buscaId, setBuscaId] = useState("")

  const [filtroEspecialidade, setFiltroEspecialidade] = useState("")

  const [novoMedico, setNovoMedico] = useState({
    name: "",
    email: "",
    phone: "",
    specialtyId: "",
    password: ""
  })

  const [medicoExcluir, setMedicoExcluir] = useState(null)

  useEffect(() => {

    carregarMedicos()

    carregarEspecialidades()

  }, [])

  function carregarMedicos() {

    api.get("/doctors")

      .then(response => {
        setMedicos(response.data.content)
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

  function salvarMedico() {

    if (medicoEditando) {

      api.put(
        `/doctors/${medicoEditando.id}`,
        novoMedico
      )

        .then(() => {

          carregarMedicos()

          limparFormulario()

        })

        .catch(error => {
          console.log(error)
        })

    } else {

      api.post("/doctors", novoMedico)

        .then(() => {

          carregarMedicos()

          limparFormulario()

        })

        .catch(error => {
          console.log(error)
        })
    }
  }

  function editarMedico(medico) {

    setMedicoEditando(medico)

    setNovoMedico({
      name: medico.name,
      email: medico.email,
      phone: medico.phone,
      specialtyId: medico.specialty.id,
      password: ""
    })

    setMostrarFormulario(true)
  }

 function excluirMedico() {

  api.delete(
    `/doctors/${medicoExcluir.id}`
  )

    .then(() => {

      carregarMedicos()

      setMedicoExcluir(null)

    })

    .catch(error => {

      console.log(error)

      alert("Erro ao excluir médico")

    })
}

  function buscarPorNome() {

    if (!buscaNome) {
      carregarMedicos()
      return
    }

    api.get(`/doctors/search?name=${buscaNome}`)

      .then(response => {
        setMedicos(response.data)
      })

      .catch(error => {
        console.log(error)
      })
  }

  function buscarPorId() {

    if (!buscaId) {
      carregarMedicos()
      return
    }

    api.get(`/doctors/${buscaId}`)

      .then(response => {

        setMedicos([response.data])

      })

      .catch(error => {

        console.log(error)

        alert("Médico não encontrado")
      })
  }

  function filtrarPorEspecialidade(id) {

    setFiltroEspecialidade(id)

    if (!id) {
      carregarMedicos()
      return
    }

    api.get(`/doctors/by-specialty?specialtyId=${id}`)

      .then(response => {
        setMedicos(response.data.content)
      })

      .catch(error => {
        console.log(error)
      })
  }

  function limparFormulario() {

    setNovoMedico({
      name: "",
      email: "",
      phone: "",
      specialtyId: "",
      password: ""
    })

    setMedicoEditando(null)

    setMostrarFormulario(false)
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-6">

        <h1 className="text-3xl font-bold">
          Médicos
        </h1>

        <button
          onClick={() => {

            limparFormulario()

            setMostrarFormulario(true)
          }}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          Novo Médico
        </button>

      </div>

      <div className="bg-white p-4 rounded-xl shadow mb-6">

        <div className="grid grid-cols-3 gap-4">

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

          <select
            value={filtroEspecialidade}
            onChange={(e) =>
              filtrarPorEspecialidade(e.target.value)
            }
            className="border p-3 rounded-lg"
          >

            <option value="">
              Todas especialidades
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

      </div>

      {
        mostrarFormulario && (

          <div className="bg-white p-6 rounded-xl shadow mb-6">

            <h2 className="text-xl font-bold mb-4">

              {
                medicoEditando
                  ? "Editar Médico"
                  : "Novo Médico"
              }

            </h2>

            <div className="grid grid-cols-2 gap-4">

              <input
                type="text"
                placeholder="Nome"
                value={novoMedico.name}
                onChange={(e) =>
                  setNovoMedico({
                    ...novoMedico,
                    name: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              />

              <input
                type="email"
                placeholder="Email"
                value={novoMedico.email}
                onChange={(e) =>
                  setNovoMedico({
                    ...novoMedico,
                    email: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              />

              <input
                type="text"
                placeholder="Telefone"
                value={novoMedico.phone}
                onChange={(e) =>
                  setNovoMedico({
                    ...novoMedico,
                    phone: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              />

              {
                !medicoEditando && (

                  <input
                    type="password"
                    placeholder="Senha"
                    value={novoMedico.password}
                    onChange={(e) =>
                      setNovoMedico({
                        ...novoMedico,
                        password: e.target.value
                      })
                    }
                    className="border p-3 rounded-lg"
                  />

                )
              }

              <select
                value={novoMedico.specialtyId}
                onChange={(e) =>
                  setNovoMedico({
                    ...novoMedico,
                    specialtyId: e.target.value
                  })
                }
                className="border p-3 rounded-lg"
              >

                <option value="">
                  Selecione a especialidade
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

            <div className="flex gap-3 mt-4">

              <button
                onClick={salvarMedico}
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
                Especialidade
              </th>

              <th className="text-left p-4">
                Email
              </th>

              <th className="text-left p-4">
                Ações
              </th>

            </tr>

          </thead>

          <tbody>

            {
              medicos.map(medico => (

                <tr
                  key={medico.id}
                  className="border-t hover:bg-gray-50"
                >

                  <td className="p-4">
                    {medico.id}
                  </td>

                  <td className="p-4 font-medium">
                    {medico.name}
                  </td>

                  <td className="p-4">

                    <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm">
                      {medico.specialty?.name}
                    </span>

                  </td>

                  <td className="p-4">
                    {medico.email}
                  </td>

                  <td className="p-4">

                    <div className="flex gap-2">

                      <button
                        onClick={() => editarMedico(medico)}
                        className="bg-yellow-400 px-3 py-1 rounded"
                      >
                        Editar
                      </button>

                      <button
                        onClick={() => setMedicoExcluir(medico)}
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
  medicoExcluir && (

    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">

      <div className="bg-white p-8 rounded-2xl w-[400px]">

        <h2 className="text-2xl font-bold mb-4">

          Excluir Médico

        </h2>

        <p className="mb-6">

          Deseja realmente excluir

          <strong>
            {" "}
            {medicoExcluir.name}
          </strong>

          ?

        </p>

        <div className="flex gap-3">

          <button
            onClick={excluirMedico}
            className="bg-red-500 text-white px-4 py-3 rounded-xl flex-1"
          >

            Excluir

          </button>

          <button
            onClick={() =>
              setMedicoExcluir(null)
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

export default Medicos
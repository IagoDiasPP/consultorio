import { useEffect, useState } from "react"
import api from "../services/api"

function Especialidade() {

  const [especialidades, setEspecialidades] = useState([])

  const [mostrarFormulario, setMostrarFormulario] = useState(false)

  const [novaEspecialidade, setNovaEspecialidade] = useState({
    name: "",
    active: true
  })
  const [especialidadeEditando, setEspecialidadeEditando] = useState(null)

  useEffect(() => {
    carregarEspecialidades()
  }, [])

  const [idBusca, setIdBusca] = useState("")

  function editarEspecialidade(especialidade) {

  setEspecialidadeEditando(especialidade)

  setNovaEspecialidade({
    name: especialidade.name,
    active: especialidade.active
  })

  setMostrarFormulario(true)
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
  function buscarPorId() {

  if (!idBusca) {
    carregarEspecialidades()
    return
  }

  api.get(`/specialties/${idBusca}`)

    .then(response => {

      setEspecialidades([response.data])

    })

    .catch(error => {

      console.log(error)

      alert("Especialidade não encontrada")
    })
}

  function salvarEspecialidade() {

  if (especialidadeEditando) {

    api.put(
      `/specialties/${especialidadeEditando.id}`,
      novaEspecialidade
    )

      .then(() => {

        carregarEspecialidades()

        setNovaEspecialidade({
          name: "",
          active: true
        })

        setEspecialidadeEditando(null)

        setMostrarFormulario(false)

      })

      .catch(error => {
        console.log(error)
      })

  } else {

    api.post("/specialties", novaEspecialidade)

      .then(response => {

        setEspecialidades([
          ...especialidades,
          response.data
        ])

        setNovaEspecialidade({
          name: "",
          active: true
        })

        setMostrarFormulario(false)

      })

      .catch(error => {
        console.log(error)
      })
  }
}

  function excluirEspecialidade(id) {

    api.delete(`/specialties/${id}`)

      .then(() => {

        const listaAtualizada =
          especialidades.filter(
            especialidade => especialidade.id !== id
          )

        setEspecialidades(listaAtualizada)

      })

      .catch(error => {
        console.log(error)
      })
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-6">

        <h1 className="text-3xl font-bold">
          Especialidades
        </h1>

        <button
          onClick={() => {

  setEspecialidadeEditando(null)

  setNovaEspecialidade({
    name: "",
    active: true
  })

  setMostrarFormulario(true)
}}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          Nova Especialidade
        </button>

      </div>

      {
        
        mostrarFormulario && (
            

          <div className="bg-white p-6 rounded-xl shadow mb-6">

            <h2 className="text-xl font-bold mb-4">

  {
    especialidadeEditando
      ? "Editar Especialidade"
      : "Nova Especialidade"
  }

</h2>

            <div className="flex gap-4">

              <input
                type="text"
                placeholder="Nome da especialidade"
                value={novaEspecialidade.name}
                onChange={(e) =>
                  setNovaEspecialidade({
                    ...novaEspecialidade,
                    name: e.target.value
                  })
                }
                className="border p-3 rounded-lg flex-1"
              />

              <select
                value={novaEspecialidade.active}
                onChange={(e) =>
                  setNovaEspecialidade({
                    ...novaEspecialidade,
                    active: e.target.value === "true"
                  })
                }
                className="border p-3 rounded-lg"
              >

                <option value={true}>
                  Ativa
                </option>

                <option value={false}>
                  Inativa
                </option>

              </select>

            </div>

            <div className="flex gap-3 mt-4">

              <button
                onClick={salvarEspecialidade}
                className="bg-green-600 text-white px-4 py-2 rounded-lg"
              >
                Salvar
              </button>

              <button
               onClick={() => {

  setMostrarFormulario(false)

  setEspecialidadeEditando(null)

  setNovaEspecialidade({
    name: "",
    active: true
  })
}}
                className="bg-gray-500 text-white px-4 py-2 rounded-lg"
              >
                Cancelar
              </button>

            </div>

          </div>
        )
      }
      <div className="bg-white p-4 rounded-xl shadow mb-6">

  <div className="flex gap-4">

    <input
      type="number"
      placeholder="Buscar por ID"
      value={idBusca}
      onChange={(e) => setIdBusca(e.target.value)}
      className="border p-3 rounded-lg"
    />

    <button
      onClick={buscarPorId}
      className="bg-blue-600 text-white px-4 py-2 rounded-lg"
    >
      Buscar
    </button>

    <button
      onClick={carregarEspecialidades}
      className="bg-gray-500 text-white px-4 py-2 rounded-lg"
    >
      Limpar
    </button>

  </div>

</div>

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
                Status
              </th>

              <th className="text-left p-4">
                Ações
              </th>

            </tr>

          </thead>

          <tbody>

            {
              especialidades.map(especialidade => (

                <tr
                  key={especialidade.id}
                  className="border-t hover:bg-gray-50"
                >

                  <td className="p-4">
                    {especialidade.id}
                  </td>

                  <td className="p-4 font-medium">
                    {especialidade.name}
                  </td>

                  <td className="p-4">

                    {
                      especialidade.active ? (

                        <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm">
                          Ativa
                        </span>

                      ) : (

                        <span className="bg-red-100 text-red-700 px-3 py-1 rounded-full text-sm">
                          Inativa
                        </span>

                      )
                    }

                  </td>

                  <td className="p-4">

  <div className="flex gap-2">

    <button
      onClick={() => editarEspecialidade(especialidade)}
      className="bg-yellow-400 px-3 py-1 rounded"
    >
      Editar
    </button>

    <button
      onClick={() => excluirEspecialidade(especialidade.id)}
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

    </div>
  )
}

export default Especialidade
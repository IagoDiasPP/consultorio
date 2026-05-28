import { useEffect, useState } from "react"

import api from "../services/api"

function FilaEspera() {

  const [fila, setFila] = useState([])

  useEffect(() => {

    carregarFila()

  }, [])

  function carregarFila() {

    api.get("/appointments/waiting")

      .then(response => {

        setFila(response.data)

      })

      .catch(error => {

        console.log(error)

      })
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-8">

        <h1 className="text-3xl font-bold">

          Fila de Espera

        </h1>

        <button
          onClick={carregarFila}
          className="bg-blue-600 text-white px-5 py-3 rounded-xl"
        >

          Atualizar

        </button>

      </div>

      {
        fila.length === 0 && (

          <div className="bg-white rounded-2xl shadow p-10 text-center text-gray-500">

            Nenhum paciente na fila de espera.

          </div>
        )
      }

      <div className="grid gap-6">

        {
          fila.map((consulta, index) => (

            <div
              key={consulta.id}
              className="bg-white rounded-2xl shadow p-6 border-l-8 border-yellow-400"
            >

              <div className="flex justify-between items-start mb-6">

                <div>

                  <h2 className="text-2xl font-bold">

                    {consulta.patient.name}

                  </h2>

                  <div className="text-gray-500 mt-1">

                    Posição na fila:
                    {" "}

                    <strong>
                      #{index + 1}
                    </strong>

                  </div>

                </div>

                <span className="bg-yellow-100 text-yellow-700 px-4 py-2 rounded-full text-sm font-bold">

                  AGUARDANDO VAGA

                </span>

              </div>

              <div className="grid md:grid-cols-2 gap-6">

                <div>

                  <div className="mb-4">

                    <div className="text-gray-500 text-sm">

                      Telefone

                    </div>

                    <div className="font-semibold">

                      {consulta.patient.phone}

                    </div>

                  </div>

                  <div className="mb-4">

                    <div className="text-gray-500 text-sm">

                      Especialidade

                    </div>

                    <div className="font-semibold">

                      {consulta.specialty.name}

                    </div>

                  </div>

                </div>

                <div>

                  <div className="mb-4">

                    <div className="text-gray-500 text-sm">

                      Status

                    </div>

                    <div className="font-semibold">

                      {consulta.status}

                    </div>

                  </div>

                  <div className="mb-4">

                    <div className="text-gray-500 text-sm">

                      ID da Consulta

                    </div>

                    <div className="font-semibold">

                      #{consulta.id}

                    </div>

                  </div>

                </div>

              </div>

            </div>
          ))
        }

      </div>

    </div>
  )
}

export default FilaEspera
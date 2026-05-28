import { useEffect, useState } from "react"

import api from "../services/api"

function ListaTelefonica() {

  const [calls, setCalls] = useState([])

  useEffect(() => {

    carregarLista()

  }, [])

  function carregarLista() {

    api.get("/appointments")

      .then(response => {

        const pendentes =
          response.data.filter(
            consulta =>
              consulta.status === "PENDING"
          )

        setCalls(pendentes)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function confirmarConsulta(consulta) {

    api.put(

      `/appointments/${consulta.id}`,

      {
        status: "CONFIRMED"
      }

    )

      .then(() => {

        carregarLista()

        alert(
          "Consulta confirmada!"
        )

      })

      .catch(error => {

        console.log(error)

        alert(
          "Erro ao confirmar consulta"
        )

      })
  }

  function cancelarConsulta(consulta) {

    api.put(

      `/appointments/${consulta.id}`,

      {
        status: "CANCELLED"
      }

    )

      .then(() => {

        api.post(
          "/appointments/process-queue"
        )

        carregarLista()

        alert(
          "Consulta cancelada!"
        )

      })

      .catch(error => {

        console.log(error)

        alert(
          "Erro ao cancelar consulta"
        )

      })
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-8">

        <h1 className="text-3xl font-bold">

          Lista Telefônica

        </h1>

      </div>

      <div className="grid gap-6">

        {
          calls.map(consulta => (

            <div
              key={consulta.id}
              className="bg-white rounded-2xl shadow p-6"
            >

              <div className="grid md:grid-cols-2 gap-6">

                <div>

                  <div className="mb-4">

                    <span className="font-bold">
                      Paciente:
                    </span>

                    <div>
                      {consulta.patient.name}
                    </div>

                  </div>

                  <div className="mb-4">

                    <span className="font-bold">
                      Telefone:
                    </span>

                    <div>
                      {consulta.patient.phone}
                    </div>

                  </div>

                  <div className="mb-4">

                    <span className="font-bold">
                      Especialidade:
                    </span>

                    <div>
                      {consulta.specialty.name}
                    </div>

                  </div>

                </div>

                <div>

                  <div className="mb-4">

                    <span className="font-bold">
                      Médico:
                    </span>

                    <div>
                      {consulta.doctor?.name}
                    </div>

                  </div>

                  <div className="mb-4">

                    <span className="font-bold">
                      Data:
                    </span>

                    <div>
                      {consulta.date}
                    </div>

                  </div>

                  <div className="mb-4">

                    <span className="font-bold">
                      Horário:
                    </span>

                    <div>
                      {consulta.startTime}
                    </div>

                  </div>

                </div>

              </div>

              <div className="flex gap-4 mt-6">

                <button
                  onClick={() =>
                    confirmarConsulta(consulta)
                  }
                  className="bg-green-600 text-white px-5 py-3 rounded-xl"
                >

                  Confirmou Presença

                </button>

                <button
                  onClick={() =>
                    cancelarConsulta(consulta)
                  }
                  className="bg-red-600 text-white px-5 py-3 rounded-xl"
                >

                  Não Confirmou

                </button>

              </div>

            </div>
          ))
        }

      </div>

    </div>
  )
}

export default ListaTelefonica
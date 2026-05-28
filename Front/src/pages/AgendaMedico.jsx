import { useEffect, useState } from "react"
import api from "../services/api"
import axios from "axios"

function AgendaMedico() {

  const [medicos, setMedicos] = useState([])

  const [doctorId, setDoctorId] = useState("")

  const [diaSelecionado, setDiaSelecionado] = useState("MONDAY")

  const [slots, setSlots] = useState([])

  const [slotSelecionado, setSlotSelecionado] = useState(null)

  const diasSemana = [
    {
      label: "Segunda",
      value: "MONDAY"
    },
    {
      label: "Terça",
      value: "TUESDAY"
    },
    {
      label: "Quarta",
      value: "WEDNESDAY"
    },
    {
      label: "Quinta",
      value: "THURSDAY"
    },
    {
      label: "Sexta",
      value: "FRIDAY"
    },
    {
      label: "Sábado",
      value: "SATURDAY"
    },
    {
      label: "Domingo",
      value: "SUNDAY"
    }
  ]

  useEffect(() => {

    carregarMedicos()

  }, [])

  useEffect(() => {

    if (doctorId) {

      carregarSlots()

    }

  }, [doctorId, diaSelecionado])

  async function remarcarConsulta(id) {

  await axios.put(
    `http://localhost:8080/appointments/${id}/remarcar`
  )

  await carregarSlots()

  setSlotSelecionado(null)
}

async function darAlta(id) {

  await axios.put(
    `http://localhost:8080/appointments/${id}/alta`
  )

  await carregarSlots()

  setSlotSelecionado(null)
}

  function carregarMedicos() {

    api.get("/doctors")

      .then(response => {

        setMedicos(response.data.content)

      })

      .catch(error => {

        console.log(error)

      })
  }

  function carregarSlots() {

    api.get(
      `/appointments/doctor-slots?doctorId=${doctorId}&dayOfWeek=${diaSelecionado}`
    )

      .then(response => {

        setSlots(response.data)

      })

      .catch(error => {

        console.log(error)

      })
  }

  return (

    <div>

      <div className="flex justify-between items-center mb-8">

        <h1 className="text-3xl font-bold">
          Agenda do Médico
        </h1>

      </div>

      <div className="bg-white p-6 rounded-2xl shadow mb-6">

        <select
          value={doctorId}
          onChange={(e) =>
            setDoctorId(e.target.value)
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
        doctorId && (

          <>

            <div className="flex gap-3 mb-8 flex-wrap">

              {
                diasSemana.map(dia => (

                  <button
                    key={dia.value}
                    onClick={() =>
                      setDiaSelecionado(dia.value)
                    }
                    className={
                      diaSelecionado === dia.value
                        ? "bg-blue-600 text-white px-5 py-3 rounded-xl"
                        : "bg-white px-5 py-3 rounded-xl shadow"
                    }
                  >

                    {dia.label}

                  </button>
                ))
              }

            </div>



            <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">

              {
                slots.map((slot) => (

                  <button
                   key={`${slot.time}-${slot.date}-${slot.available}`}
                    onClick={() => {

  console.log(slot)

  if (!slot.available) {

    setSlotSelecionado(slot)

  }

}}
                    className={
                      slot.available

                        ? "bg-green-500 text-white p-5 rounded-2xl font-bold text-lg hover:scale-105 transition"

                        : "bg-red-500 text-white p-5 rounded-2xl font-bold text-lg hover:scale-105 transition"
                    }
                  >

                    <div>

                      {slot.time}

                    </div>

                    <div className="text-sm mt-2">

                      {
                        slot.available
                          ? "Disponível"
                          : "Ocupado"
                      }

                    </div>

                  </button>
                ))
              }

            </div>

          </>
        )
      }

      {
        slotSelecionado && (

          <div className="fixed inset-0 bg-black/50 flex items-center justify-center">

            <div className="bg-white p-8 rounded-2xl w-[400px]">

              <h2 className="text-2xl font-bold mb-6">

                Consulta

              </h2>

              <div className="space-y-4">

                <div>

                  <strong>
                    Horário:
                  </strong>

                  <div>
                    {slotSelecionado.time}
                  </div>

                </div>

                <div>
  <strong>Data:</strong>
  <div>
    {slotSelecionado.date}
  </div>
</div>

                <div>

                  <strong>
                    Paciente:
                  </strong>

                  <div>
                    {slotSelecionado.patientName}
                  </div>

                </div>

                <div>

                  <strong>
                    Telefone:
                  </strong>

                  <div>
                    {slotSelecionado.patientPhone}
                  </div>

                </div>

                <div>

                  <strong>
                    Especialidade:
                  </strong>

                  <div>
                    {slotSelecionado.specialtyName}
                  </div>

                  <div className="flex gap-3 mt-6">

  <button
    onClick={() =>
      remarcarConsulta(
        slotSelecionado.appointmentId
      )
    }
    className="
      bg-yellow-500
      text-white
      px-5
      py-3
      rounded-xl
    "
  >

    Remarcar Próxima Semana

  </button>

  <button
    onClick={() =>
      darAlta(
        slotSelecionado.appointmentId
      )
    }
    className="
      bg-green-600
      text-white
      px-5
      py-3
      rounded-xl
    "
  >

    Dar Alta

  </button>

</div>

                </div>

                <div>

                  <strong>
                    Status:
                  </strong>

                  <div>
                    {slotSelecionado.status}
                  </div>

                </div>

              </div>

              <button
                onClick={() =>
                  setSlotSelecionado(null)
                }
                className="bg-gray-600 text-white px-4 py-2 rounded-xl mt-6 w-full"
              >

                Fechar

              </button>

            </div>

          </div>
        )
      }

    </div>
  )
}

export default AgendaMedico
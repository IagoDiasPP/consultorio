import { useEffect, useState } from "react"
import api from "../services/api"

function AgendaMedico() {

  const [mesAtual, setMesAtual] = useState(new Date())

  const [medicos, setMedicos] = useState([])

  const [doctorId, setDoctorId] = useState("")

  const [dataSelecionada, setDataSelecionada] = useState("")

  const [slots, setSlots] = useState([])

  const [slotSelecionado, setSlotSelecionado] = useState(null)



  useEffect(() => {

    carregarMedicos()

  }, [])

  useEffect(() => {
    if (doctorId && dataSelecionada) {
      carregarSlots()
    }
  }, [doctorId, dataSelecionada])

  async function remarcarConsulta(id) {
    await api.put(
      `/appointments/${id}/remarcar`
    )
    await carregarSlots()
    setSlotSelecionado(null)
  }

async function darAlta(id) {
  await api.put(
    `/appointments/${id}/alta`
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
      `/appointments/doctor-slots?doctorId=${doctorId}&date=${dataSelecionada}`
    )
      .then(response => {
        setSlots(response.data)
      })
      .catch(error => {
        console.log(error)
      })
  }

function mudarMes(valor) {
  setMesAtual(
    new Date(
      mesAtual.getFullYear(),
      mesAtual.getMonth() + valor,
      1
    )
  )
}

function gerarDiasDoMes() {
  const ano = mesAtual.getFullYear()
  const mes = mesAtual.getMonth()

  const primeiroDia = new Date(ano, mes, 1)
  const ultimoDia = new Date(ano, mes + 1, 0)

  const dias = []

  for (let i = 0; i < primeiroDia.getDay(); i++) {
    dias.push(null)
  }

  for (let dia = 1; dia <= ultimoDia.getDate(); dia++) {
    dias.push(
      new Date(ano, mes, dia)
    )
  }

  return dias
}

function selecionarData(data) {
  const ano = data.getFullYear()
  const mes = String(data.getMonth() + 1).padStart(2, "0")
  const dia = String(data.getDate()).padStart(2, "0")

  setDataSelecionada(`${ano}-${mes}-${dia}`)
}

function formatarData(data) {
  const ano = data.getFullYear()
  const mes = String(data.getMonth() + 1).padStart(2, "0")
  const dia = String(data.getDate()).padStart(2, "0")

  return `${ano}-${mes}-${dia}`
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
          onChange={(e) => {
            const id = e.target.value

            setDoctorId(id)

            if (id) {
              const hoje = new Date()

              setMesAtual(new Date(hoje.getFullYear(), hoje.getMonth(), 1))

              const ano = hoje.getFullYear()
              const mes = String(hoje.getMonth() + 1).padStart(2, "0")
              const dia = String(hoje.getDate()).padStart(2, "0")

              setDataSelecionada(`${ano}-${mes}-${dia}`)
            } else {
              setDataSelecionada("")
            }
          }}
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

            <div className="bg-white p-6 rounded-2xl shadow mb-8">

              <div className="flex justify-between items-center mb-6">

                <button
                  onClick={() => mudarMes(-1)}
                  className="px-4 py-2 bg-gray-200 rounded-xl"
                >
                  ←
                </button>

                <h2 className="text-xl font-bold">
                  {mesAtual.toLocaleDateString("pt-BR", {
                    month: "long",
                    year: "numeric"
                  })}
                </h2>

                <button
                  onClick={() => mudarMes(1)}
                  className="px-4 py-2 bg-gray-200 rounded-xl"
                >
                  →
                </button>

              </div>

              <div className="grid grid-cols-7 gap-2 mb-2 text-center font-bold">

                <div>Dom</div>
                <div>Seg</div>
                <div>Ter</div>
                <div>Qua</div>
                <div>Qui</div>
                <div>Sex</div>
                <div>Sáb</div>

              </div>

              <div className="grid grid-cols-7 gap-2">
                {gerarDiasDoMes().map((data, index) => (
                  <div key={index}>
                    {data && (
                      <button
                        onClick={() => selecionarData(data)}
                        className={`w-full p-3 rounded-xl hover:bg-gray-200 ${
                          dataSelecionada === formatarData(data)
                            ? "bg-blue-600 text-white"
                            : ""
                        }`}
                      >
                        {data.getDate()}
                      </button>
                    )}
                  </div>
                ))}
              </div>

            {dataSelecionada && (
              <div className="mb-4">
                <h2 className="text-xl font-bold">
                  Consultas do dia{" "}
                  {new Date(`${dataSelecionada}T00:00:00`).toLocaleDateString(
                    "pt-BR"
                  )}
                </h2>
              </div>
            )}

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
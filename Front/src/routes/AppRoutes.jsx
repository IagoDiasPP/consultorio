import { BrowserRouter, Routes, Route } from "react-router-dom"
import Especialidade from "../pages/Especialidade"

import MainLayout from "../layouts/MainLayout"
import Dashboard from "../pages/Dashboard"
import Medicos from "../pages/Medicos"
import Pacientes from "../pages/Pacientes"
import Consultas from "../pages/Consultas"
import FilaEspera from "../pages/FilaEspera"
import AgendaMedico from "../pages/AgendaMedico"
import Schedules from "../pages/Schedules"
import ListaTelefonica from "../pages/ListaTelefonica"

function AppRoutes() {
  return (
    <BrowserRouter>

      <MainLayout>

        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/medicos" element={<Medicos />} />
          <Route path="/pacientes" element={<Pacientes />} />
          <Route path="/consultas" element={<Consultas />} />
          <Route path="/fila-espera" element={<FilaEspera />} />
          <Route path="/especialidades" element={<Especialidade />} />
          <Route path="/agenda" element={<AgendaMedico />} />
          <Route path="/schedules" element={<Schedules />} />
          <Route path="/lista-telefonica" element={<ListaTelefonica />}/>
        </Routes>

      </MainLayout>

    </BrowserRouter>
  )
}

export default AppRoutes
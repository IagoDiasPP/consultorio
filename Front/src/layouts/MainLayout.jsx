import { Link } from "react-router-dom"

function MainLayout({ children }) {
  return (
    <div className="flex h-screen">

      {/* Sidebar */}
      <aside className="w-64 bg-blue-900 text-white p-6">

        <h1 className="text-2xl font-bold mb-10">
          Sistema Clínica
        </h1>

        <nav className="flex flex-col gap-4">

          <Link
            to="/"
            className="hover:text-blue-300"
          >
            Dashboard
            
          </Link>

          <Link
            to="/medicos"
            className="hover:text-blue-300"
          >
            Médicos
          </Link>

          <Link
            to="/pacientes"
            className="hover:text-blue-300"
          >
            Pacientes
          </Link>

          <Link
            to="/consultas"
            className="hover:text-blue-300"
          >
            Consultas
          </Link>

          <Link
            to="/fila-espera"
            className="hover:text-blue-300"
          >
            Fila de Espera
          </Link>
          <Link
  to="/especialidades"
  className="hover:text-blue-300"
>
  Especialidades
</Link>

<Link
  to="/agenda"
  className="hover:text-blue-300"
>
  Agenda
</Link>



<Link
  to="/schedules"
  className="hover:text-blue-300"
>
  Agenda Médica
</Link>

<Link
  to="/lista-telefonica"
  className="hover:text-blue-300"
>
  Lista Telefônica
</Link>

        </nav>
      </aside>

      {/* Conteúdo */}
      <main className="flex-1 p-8 bg-gray-100">
        {children}
      </main>

    </div>
  )
}



export default MainLayout
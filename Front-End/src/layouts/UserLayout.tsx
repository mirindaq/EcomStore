
import Footer from "@/components/user/Footer/Footer";
import Header from "@/components/user/Header/Header";
import { Outlet } from "react-router";

export default function UserLayout() {
  return (
    <>
      <Header />
      {/* <main style={{ backgroundImage: "url('/images/background.webp')" }}> */}
      <main className="min-h-screen my-5 mx-auto max-w-7xl">
        <Outlet />
      </main>
      <Footer />
    </>
  )
}

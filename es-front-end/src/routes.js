import Home from './components/Home';
import About from './components/AboutUs';
import Contact from './components/ContactUs';

const routes = [
  { path: '/', component: <Home />, exact: true },
  { path: '/about', component: <About /> },
  { path: '/contact', component: <Contact /> },
];

export default routes;
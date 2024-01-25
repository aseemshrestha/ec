import React from "react";
import { useForm } from "react-hook-form";
import { Link } from 'react-router-dom';

export default function RegistrationForm() {
   const { register, handleSubmit, formState: { errors } } = useForm();
   const onSubmit = (data) => {
      alert(JSON.stringify(data));
   };

   return (
      <form className="login_form" onSubmit={handleSubmit(onSubmit)}>
         <div className="row">
            <div className="col-md-12">
               <p className="form-box">
                  <input {...register("email", { required: true, pattern: /^[^ ]+@[^ ]+\.[a-z]{2,6}$/i })} type="email" placeholder="Email Address" className="form-control" />
                  {errors?.email?.type === "required" && <label className="error">This field is required</label>}
                  {errors?.email?.type === "pattern" && <label className="error">Incorrect email address</label>}
               </p>
            </div>
            <div className="col-md-12">
               <p className="form-box">
                  <input {...register("password", { required: true, minLength: 8, maxLength: 12, pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,12}$/i })} type="password" placeholder="Password" className="form-control" />
                  {errors?.password?.type === "required" && <label className="error">This field is required</label>}
                  {errors?.password?.type === "minLength" && <label className="error">Password should be 8 characters long</label>}
                  {errors?.password?.type === "maxLength" && <label className="error">Password cannot exceed 12 characters</label>}
                  {errors?.password?.type === "pattern" && <label className="error">Password must contain atleast 1 speacial character and digit and alphabet</label>}
               </p>
            </div>
            <div className="col-md-12">
               <div className="save-forget-password d-flex justify-content-between">
                  <p className="keep-loged">
                     <label className="choice" htmlFor="keep_loged">
                        <input {...register("keeplog", { required: false })} type="checkbox" id="keep_loged" />
                        Keep me loged in
                     </label>
                  </p>
                  <p className="forget-pass">
                     <Link to={process.env.PUBLIC_URL + "/"}>Forget Password?</Link>
                  </p>
               </div>
            </div>
            <div className="col-md-12">
               <button className="submit-btn btn btn-primary" type="submit">Register</button>
            </div>
            <div className="col-md-12">
               <div className="signup-btn text-center">
                  <p>New to Afily? <Link to={process.env.PUBLIC_URL + "/register"}>Sign Up</Link></p>
               </div>
               <div className="social-login text-center">
                  <span>OR</span>
                  <p>Sign up with social</p>
                  <ul className="list-unstyled list-inline">
                     <li className="list-inline-item"><a href={process.env.PUBLIC_URL + "/"}><i className="fab fa-google"></i> Google</a></li>
                     <li className="list-inline-item"><a href={process.env.PUBLIC_URL + "/"}><i className="fab fa-facebook-f"></i> Facebook</a></li>
                     <li className="list-inline-item"><a href={process.env.PUBLIC_URL + "/"}><i className="fab fa-twitter"></i> Twitter</a></li>
                  </ul>
               </div>
            </div>
         </div>
      </form>
   );
}
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { ViewProductsComponent } from './components/view-products/view-products.component';
/* Routing for the web app is here. */
const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'sign-up',
    component: SignUpComponent,
  },
  {
    path: 'view-products',
    component: ViewProductsComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

/* Add import to app component as an array of components */
export const routingComponents=[
  LoginComponent,
  SignUpComponent,
  ViewProductsComponent,
]
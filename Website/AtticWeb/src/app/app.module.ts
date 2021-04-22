import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule, routingComponents } from './app-routing.module';
import { AppComponent } from './app.component';

// importing firebase modules for database purposes.
import { AngularFireModule } from "@angular/fire";
import { AngularFirestoreModule } from "@angular/fire/firestore";

import { AngularFireStorageModule,} from "@angular/fire/storage";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// import forms module to handle forms
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// angular material imports
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { FileUploadComponent } from './components/file-upload/file-upload.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { FurnitureViewComponent } from './components/furniture-view/furniture-view.component';

// firebase configurations
const firebaseConfig = {
  apiKey: "AIzaSyCU_hDV3QZMRcEJ9Z7piImaY48KqOvVlsI",
  authDomain: "attic-b6655.firebaseapp.com",
  projectId: "attic-b6655",
  storageBucket: "attic-b6655.appspot.com",
  messagingSenderId: "109489326265",
  appId: "1:109489326265:web:e502f9c6c27966d9447368",
  measurementId: "G-9DCTKH9SCF"
};

@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    FileUploadComponent,
    HeaderComponent,
    FurnitureViewComponent,
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,

    AngularFireModule.initializeApp(firebaseConfig),
    AngularFirestoreModule,
    AngularFireStorageModule,
    

    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,

    MatProgressBarModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatCheckboxModule,
    MatButtonModule,
    MatInputModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatFormFieldModule,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }



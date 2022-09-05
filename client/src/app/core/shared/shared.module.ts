import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormErrorComponent } from './form-error/form-error.component';
import { LoadingComponent } from './loading/loading.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [FormErrorComponent, LoadingComponent],
  imports: [
    CommonModule,
    MatProgressSpinnerModule
  ],
  exports: [
    FormErrorComponent,
    LoadingComponent,
    MatIconModule
  ]
})
export class SharedModule { }

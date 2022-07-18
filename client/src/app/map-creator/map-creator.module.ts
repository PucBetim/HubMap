import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CreatorRoutes } from './map-creator.routing';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(CreatorRoutes)
  ]
})
export class MapCreatorModule { }

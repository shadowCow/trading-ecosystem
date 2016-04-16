import {Component} from 'angular2/core';
import {PriceChartComponent} from '../price_chart/price-chart.component';

@Component({
  selector: 'app-container',
  templateUrl: '/dist/components/app_container/app-container.html',
  styleUrls: ['/dist/components/app_container/app-container.css'],
  directives: [PriceChartComponent]
})
export class AppContainerComponent {

}

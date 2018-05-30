// Hello…
// This is a backport of our old chart-js data api plugin
// We no longer provide this as a supported plugin because of changes
// to the chart.js option api. However, for contrived examples (like the ones
// found in our theme examples, it makes our lives a bit easier.)
// if you're reading this, we *HIGHLY* recommend you don't use this in
// production, but rather implement your charts using the chart.js docs-top
// themselves, directly: www.chartjs.org/docs/
$(function () {

  var Charts = {

    _HYPHY_REGEX: /-([a-z])/g,

    _cleanAttr: function (obj) {
      delete obj["chart"]
      delete obj["datasets"]
      delete obj["datalabel"]
      delete obj["datasetsOptions"]
      delete obj["labels"]
      delete obj["options"]
    },

    doughnut: function (element) {
      var attrData = $.extend({}, $(element).data())

      var data        = attrData.dataset        ? eval(attrData.dataset) : {}
      var dataOptions = attrData.datasetOptions ? eval('(' + attrData.datasetOptions + ')') : {}
      var labels      = attrData.labels         ? eval(attrData.labels) : {}
      var options     = attrData.options        ? eval('(' + attrData.options + ')') : {}

      Charts._cleanAttr(attrData)

      var datasets = $.extend({
        data: data,
        borderWidth: 2,
        hoverBorderColor: 'transparent'
      }, dataOptions)

      var options = $.extend({
        cutoutPercentage: 80,
        legend: {
          display: false
        },
        animation: {
          animateRotate: false,
          duration: 0
        }
      }, options)

      new Chart(element.getContext('2d'), {
          type:"doughnut",
          data: {
            datasets: [ datasets ],
            labels: labels
          },
          options: options
      })
    },

    'spark-line': function (element) {
      var attrData = $.extend({}, $(element).data())

      var data           = attrData.dataset        ? eval(attrData.dataset) : []
      var datasetOptions = attrData.datasetOptions ? eval(attrData.datasetOptions) : []
      var labels         = attrData.labels         ? eval(attrData.labels) : {}
      var options     = attrData.options        ? eval('(' + attrData.options + ')') : {}

      var data = {
        labels   : labels,
        datasets : data.map(function (set, i) {
          return $.extend({
            data: set,
            fill: true,
            backgroundColor: 'rgba(255,255,255,.3)',
            borderColor: '#fff',
            pointBorderColor: '#fff',
            lineTension : 0.25,
            pointRadius: 0
          }, datasetOptions[i])
        })
      }

      Charts._cleanAttr(attrData)

      var options = $.extend({
        animation: {
          duration: 0
        },
        legend: {
          display: false
        },
        scales: {
          xAxes: [{
            display: false
          }],
          yAxes: [{
            display: false
          }]
        },
        tooltips: {
          enabled: false
        }
      }, options)

      new Chart(element.getContext('2d'), {
          type: 'line',
          data: data,
          options: options
      })
    },

    line: function (element) {
      var attrData = $.extend({}, $(element).data())

      var data           = attrData.dataset        ? eval(attrData.dataset) : []
      var dataLabels     = attrData.datalabel      ? eval(attrData.datalabel) : []
      var datasetOptions = attrData.datasetOptions ? eval(attrData.datasetOptions) : []
      var labels         = attrData.labels         ? eval(attrData.labels) : {}
      var options        = attrData.options        ? eval('(' + attrData.options + ')') : {}
      var isDark         = !!attrData.dark

      var data = {
        labels   : labels,
        datasets : data.map(function (set, i) {
          return $.extend({
            data: set,
            fill: true,
            backgroundColor: isDark ? 'rgba(28,168,221,.03)' : 'rgba(66,165,245,.2)',
            borderColor: '#42a5f5',
            pointBorderColor: '#fff',
            lineTension : 0.25,
            pointRadius: 0,
            pointHoverRadius: 0,
            pointHitRadius: 20
          }, datasetOptions[i])
        })
      }

      Charts._cleanAttr(attrData)

      var options = $.extend({
        maintainAspectRatio: false,
        animation: {
          duration: 0
        },
        legend: {
          display: false
        },
        scales: {
          yAxes: [{
            gridLines: {
              color: isDark ? 'rgba(255,255,255,.05)' : 'rgba(0,0,0,.05)',
              zeroLineColor: isDark ? 'rgba(255,255,255,.05)' : 'rgba(0,0,0,.05)',
              drawBorder: false
            },
            ticks: {
              beginAtZero: false,
              fixedStepSize: 1000,
              fontColor: isDark ? '#a2a2a2' : 'rgba(0,0,0,.4)',
              fontSize: 14
            }
          }],
          xAxes: [{
            gridLines: {
              display: false
            },
            ticks: {
              fontColor: isDark ? '#a2a2a2' : 'rgba(0,0,0,.4)',
              fontSize: 14
            }
          }]
        },
        tooltips: {
          enabled: true,
          bodyFontSize: 14,
          callbacks: {
            title: function () { return "" },
            labelColor: function () {
              return {
                backgroundColor: '#42a5f5',
                borderColor: '#42a5f5'
              }
            }
          }
        }
      }, options)

      new Chart(element.getContext('2d'), {
          type: 'line',
          data: data,
          options: options
      })
    },

    bar: function (element) {
      var attrData = $.extend({}, $(element).data())

      var data           = attrData.dataset        ? eval(attrData.dataset) : []
      var datasetOptions = attrData.datasetOptions ? eval(attrData.datasetOptions) : []
      var labels         = attrData.labels         ? eval(attrData.labels) : {}
      var options        = attrData.options        ? eval('(' + attrData.options + ')') : {}
      var isDark         = !!attrData.dark

      var data = {
        labels   : labels,
        datasets : data.map(function (set, i) {
          return $.extend({
            data: set,
            fill: true,
            backgroundColor: (i % 2 ? '#42a5f5' : '#1bc98e'),
            borderColor: 'transparent'
          }, datasetOptions[i])
        })
      }

      Charts._cleanAttr(attrData)

      var options = $.extend({
        maintainAspectRatio: false,
        animation: {
          duration: 0
        },
        legend: {
          display: false
        },
        scales: {
          yAxes: [{
            gridLines: {
              color: isDark ? 'rgba(255,255,255,.05)' : 'rgba(0,0,0,.05)',
              zeroLineColor: isDark ? 'rgba(255,255,255,.05)' : 'rgba(0,0,0,.05)',
              drawBorder: false
            },
            ticks: {
              fixedStepSize: 25,
              fontColor: isDark ? '#a2a2a2' : 'rgba(0,0,0,.4)',
              fontSize: 14
            }
          }],
          xAxes: [{
            gridLines: {
              display: false
            },
            ticks: {
              fontColor: isDark ? '#a2a2a2' : 'rgba(0,0,0,.4)',
              fontSize: 14
            }
          }]
        },
        tooltips: {
          enabled: true,
          bodyFontSize: 14
        }
      }, options)

      new Chart(element.getContext('2d'), {
          type: 'bar',
          data: data,
          options: options
      })
    }
  }

  $(document).ready(function() {
//    $('[data-chart]').each(function () {
//        Charts[$(this).attr('data-chart')](this)
//        $(this).addClass('js-chart-drawn')
//    })
    $(document).on('shown.bs.tab', function () {
      $(document).trigger('redraw.bs.charts')
    })

    $(document)
        .on('redraw.bs.charts', function () {
          $('[data-chart]').each(function () {
            if ($(this).is(':visible') && !$(this).hasClass('js-chart-drawn')) {
              Charts[$(this).attr('data-chart')](this)
              $(this).addClass('js-chart-drawn')
            }
          })
        })
        .trigger('redraw.bs.charts')
  })
});

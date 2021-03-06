﻿(function ($) {
    $.fn.cImageLoader = function (options) {
        options = $.extend({
            width: 0,
            height: 0,
            lwidth: 16,
            lheight: 16,
            loadimg: "/images/loading.gif",
            margin:0
        }, options);
        return this.each(function () {
            var target = $(this);
            var src = $(this).attr("src");
            var img = new Image();

            img.src = src;

            var scalePicWidth = function () {
                if (img.width > 0 && img.height > 0) {
                    return Math.floor((options.height == 0 ? 1 : options.height) / img.height * img.width);
                }
                else {
                    return img.width;
                }
            };
            var scalePicHeight = function () {
                if (img.width > 0 && img.height > 0) {
                    return Math.floor((options.width == 0 ? 1 : options.width) / img.width * img.height);
                }
                else {
                    return img.height;
                }
            };
            var displayWidth = function () {
                var ws = options.width / img.width;
                var hs = options.height / img.height;
                if (ws < hs) {
                    return options.width;
                }
                else {
                    return scalePicWidth(options.height);
                }
            };
            var displayHeight = function () {
                var ws = options.width / img.width;
                var hs = options.height / img.height;
                if (hs < ws) {
                    return options.height;
                }
                else {
                    return scalePicHeight(options.width);
                }
            };
            var scalePic = function (image, maxWidth, maxHeight, scaleWidth, scaleHeight) {
                var topMargin = 0;
                var leftMargin = 0;
                if (maxWidth > scaleWidth) {
                    leftMargin = Math.floor((maxWidth - scaleWidth) / 2);
                }
                if (maxHeight > scaleHeight) {
                    topMargin = Math.floor((maxHeight - scaleHeight) / 2);
                }
                var margin = options.margin;
                var newMargin;
                if (margin) {
                    margin = margin.replace(/px/g, "");
                    var marginValues = margin.split(" ");
                    var itemValue;
                    switch (marginValues.length) {
                        case 1:
                            newMargin = (topMargin + Number(marginValues[0])) + "px " + (leftMargin + Number(marginValues[0])) + "px";
                            break;
                        case 2:
                            newMargin = (topMargin + Number(marginValues[0])) + "px " + (leftMargin + Number(marginValues[1])) + "px";
                            break;
                        case 4:
                            newMargin = (topMargin + Number(marginValues[0])) + "px "
                            + (leftMargin + Number(marginValues[1])) + "px" +
                            (topMargin + Number(marginValues[2])) + "px " +
                            (leftMargin + Number(marginValues[3])) + "px";
                            break;
                        default:
                            newMargin = topMargin + "px " + leftMargin + "px";
                            break;
                    }
                }
                else {
                    newMargin = topMargin + "px " + leftMargin + "px";
                }
                image.css({ width: scaleWidth + "px", height: scaleHeight + "px", margin: newMargin });
            };
            //处理ff下会自动读取缓存图片
            if (img.complete) {
                scalePic(target, options.width, options.height, displayWidth(), displayHeight());
                return;
            }

            $(this).attr("src", "");
            var loading = $("<img alt=\"加载中\" title=\"图片加载中\" src=\"" + options.loadimg + "\" />");

            target.hide();
            target.after(loading);

            scalePic(loading, options.width, options.height, options.lwidth, options.lheight);

            $(img).load(function () {
                scalePic(target, options.width, options.height, displayWidth(), displayHeight());
                loading.remove();
                target.attr("src", this.src);
                target.show();
            });

        });
    };
})(jQuery);
"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _lodash = _interopRequireDefault(require("lodash"));
var _react = _interopRequireWildcard(require("react"));
var _reactNative = require("react-native");
var _toolkits = require("../../toolkits");
var _useDeviceOrientation = require("../../useDeviceOrientation");
var _TextInput = _interopRequireDefault(require("../TextInput"));
var _styles = require("./styles");
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
function _extends() { _extends = Object.assign ? Object.assign.bind() : function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }
const {
  isTablet
} = _toolkits.useDetectDevice;
const ic_down = require('../../assets/down.png');
const statusBarHeight = _reactNative.StatusBar.currentHeight || 0;
const DropdownComponent = /*#__PURE__*/_react.default.forwardRef((props, currentRef) => {
  const orientation = (0, _useDeviceOrientation.useDeviceOrientation)();
  const {
    testID,
    itemTestIDField,
    onChange,
    style = {},
    containerStyle,
    placeholderStyle,
    selectedTextStyle,
    itemContainerStyle,
    itemTextStyle,
    inputSearchStyle,
    iconStyle,
    selectedTextProps = {},
    data = [],
    labelField,
    valueField,
    searchField,
    value,
    activeColor = '#F6F7F8',
    fontFamily,
    iconColor = 'gray',
    searchPlaceholder,
    placeholder = 'Select item',
    search = false,
    maxHeight = 340,
    minHeight = 0,
    disable = false,
    keyboardAvoiding = true,
    inverted = true,
    renderLeftIcon,
    renderRightIcon,
    renderItem,
    renderInputSearch,
    onFocus,
    onBlur,
    autoScroll = true,
    showsVerticalScrollIndicator = true,
    dropdownPosition = 'auto',
    flatListProps,
    searchQuery,
    backgroundColor,
    onChangeText,
    confirmSelectItem,
    onConfirmSelectItem,
    accessibilityLabel,
    itemAccessibilityLabelField,
    mode = 'default'
  } = props;
  const ref = (0, _react.useRef)(null);
  const refList = (0, _react.useRef)(null);
  const [visible, setVisible] = (0, _react.useState)(false);
  const [currentValue, setCurrentValue] = (0, _react.useState)(null);
  const [listData, setListData] = (0, _react.useState)(data);
  const [position, setPosition] = (0, _react.useState)();
  const [keyboardHeight, setKeyboardHeight] = (0, _react.useState)(0);
  const [searchText, setSearchText] = (0, _react.useState)('');
  const {
    width: W,
    height: H
  } = _reactNative.Dimensions.get('window');
  const styleContainerVertical = (0, _react.useMemo)(() => {
    return {
      backgroundColor: 'rgba(0,0,0,0.1)',
      alignItems: 'center'
    };
  }, []);
  const styleHorizontal = (0, _react.useMemo)(() => {
    return {
      width: orientation === 'LANDSCAPE' ? W / 2 : '100%',
      alignSelf: 'center'
    };
  }, [W, orientation]);
  (0, _react.useImperativeHandle)(currentRef, () => {
    return {
      open: eventOpen,
      close: eventClose
    };
  });
  (0, _react.useEffect)(() => {
    return eventClose;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  (0, _react.useEffect)(() => {
    setListData([...data]);
    if (searchText) {
      onSearch(searchText);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data, searchText]);
  const eventOpen = () => {
    if (!disable) {
      setVisible(true);
      if (onFocus) {
        onFocus();
      }
      if (searchText.length > 0) {
        onSearch(searchText);
      }
      scrollIndex();
    }
  };
  const eventClose = (0, _react.useCallback)(() => {
    if (!disable) {
      setVisible(false);
      if (onBlur) {
        onBlur();
      }
    }
  }, [disable, onBlur]);
  const font = (0, _react.useCallback)(() => {
    if (fontFamily) {
      return {
        fontFamily: fontFamily
      };
    } else {
      return {};
    }
  }, [fontFamily]);
  const _measure = (0, _react.useCallback)(() => {
    if (ref && ref !== null && ref !== void 0 && ref.current) {
      ref.current.measureInWindow((pageX, pageY, width, height) => {
        let isFull = isTablet ? false : mode === 'modal' || orientation === 'LANDSCAPE';
        if (mode === 'auto') {
          isFull = false;
        }
        const top = isFull ? 20 : height + pageY + 2;
        const bottom = H - top + height;
        const left = _reactNative.I18nManager.isRTL ? W - width - pageX : pageX;
        setPosition({
          isFull,
          width: Math.floor(width),
          top: Math.floor(top + statusBarHeight),
          bottom: Math.floor(bottom - statusBarHeight),
          left: Math.floor(left),
          height: Math.floor(height)
        });
      });
    }
  }, [H, W, orientation, mode]);
  const onKeyboardDidShow = (0, _react.useCallback)(e => {
    _measure();
    setKeyboardHeight(e.endCoordinates.height);
  }, [_measure]);
  const onKeyboardDidHide = (0, _react.useCallback)(() => {
    setKeyboardHeight(0);
    _measure();
  }, [_measure]);
  (0, _react.useEffect)(() => {
    const susbcriptionKeyboardDidShow = _reactNative.Keyboard.addListener('keyboardDidShow', onKeyboardDidShow);
    const susbcriptionKeyboardDidHide = _reactNative.Keyboard.addListener('keyboardDidHide', onKeyboardDidHide);
    return () => {
      if (typeof (susbcriptionKeyboardDidShow === null || susbcriptionKeyboardDidShow === void 0 ? void 0 : susbcriptionKeyboardDidShow.remove) === 'function') {
        susbcriptionKeyboardDidShow.remove();
      }
      if (typeof (susbcriptionKeyboardDidHide === null || susbcriptionKeyboardDidHide === void 0 ? void 0 : susbcriptionKeyboardDidHide.remove) === 'function') {
        susbcriptionKeyboardDidHide.remove();
      }
    };
  }, [onKeyboardDidHide, onKeyboardDidShow]);
  const getValue = (0, _react.useCallback)(() => {
    const defaultValue = typeof value === 'object' ? _lodash.default.get(value, valueField) : value;
    const getItem = data.filter(e => _lodash.default.isEqual(defaultValue, _lodash.default.get(e, valueField)));
    if (getItem.length > 0) {
      setCurrentValue(getItem[0]);
    } else {
      setCurrentValue(null);
    }
  }, [data, value, valueField]);
  (0, _react.useEffect)(() => {
    getValue();
  }, [value, data, getValue]);
  const scrollIndex = (0, _react.useCallback)(() => {
    if (autoScroll && data.length > 0 && listData.length === data.length) {
      setTimeout(() => {
        if (refList && refList !== null && refList !== void 0 && refList.current) {
          const defaultValue = typeof value === 'object' ? _lodash.default.get(value, valueField) : value;
          const index = _lodash.default.findIndex(listData, e => _lodash.default.isEqual(defaultValue, _lodash.default.get(e, valueField)));
          if (index > -1 && index <= listData.length - 1) {
            var _refList$current;
            refList === null || refList === void 0 ? void 0 : (_refList$current = refList.current) === null || _refList$current === void 0 ? void 0 : _refList$current.scrollToIndex({
              index: index,
              animated: false
            });
          }
        }
      }, 200);
    }
  }, [autoScroll, data.length, listData, value, valueField]);
  const showOrClose = (0, _react.useCallback)(() => {
    if (!disable) {
      if (keyboardHeight > 0 && visible) {
        return _reactNative.Keyboard.dismiss();
      }
      _measure();
      setVisible(!visible);
      setListData(data);
      if (!visible) {
        if (onFocus) {
          onFocus();
        }
      } else {
        if (onBlur) {
          onBlur();
        }
      }
      if (searchText.length > 0) {
        onSearch(searchText);
      }
      scrollIndex();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [disable, keyboardHeight, visible, _measure, data, searchText, scrollIndex, onFocus, onBlur]);
  const onSearch = (0, _react.useCallback)(text => {
    if (text.length > 0) {
      const defaultFilterFunction = e => {
        var _$get;
        const item = (_$get = _lodash.default.get(e, searchField || labelField)) === null || _$get === void 0 ? void 0 : _$get.toLowerCase().replace(' ', '').normalize('NFD').replace(/[\u0300-\u036f]/g, '');
        const key = text.toLowerCase().replace(' ', '').normalize('NFD').replace(/[\u0300-\u036f]/g, '');
        return item.indexOf(key) >= 0;
      };
      const propSearchFunction = e => {
        const labelText = _lodash.default.get(e, searchField || labelField);
        return searchQuery === null || searchQuery === void 0 ? void 0 : searchQuery(text, labelText);
      };
      const dataSearch = data.filter(searchQuery ? propSearchFunction : defaultFilterFunction);
      setListData(dataSearch);
    } else {
      setListData(data);
    }
  }, [data, searchField, labelField, searchQuery]);
  const onSelect = (0, _react.useCallback)(item => {
    if (confirmSelectItem && onConfirmSelectItem) {
      return onConfirmSelectItem(item);
    }
    if (onChangeText) {
      setSearchText('');
      onChangeText('');
    }
    onSearch('');
    setCurrentValue(item);
    onChange(item);
    eventClose();
  }, [confirmSelectItem, eventClose, onChange, onChangeText, onConfirmSelectItem, onSearch]);
  const _renderDropdown = () => {
    const isSelected = currentValue && _lodash.default.get(currentValue, valueField);
    return /*#__PURE__*/_react.default.createElement(_reactNative.TouchableWithoutFeedback, {
      testID: testID,
      accessible: !!accessibilityLabel,
      accessibilityLabel: accessibilityLabel,
      onPress: showOrClose
    }, /*#__PURE__*/_react.default.createElement(_reactNative.View, {
      style: _styles.styles.dropdown
    }, renderLeftIcon === null || renderLeftIcon === void 0 ? void 0 : renderLeftIcon(visible), /*#__PURE__*/_react.default.createElement(_reactNative.Text, _extends({
      style: [_styles.styles.textItem, isSelected !== null ? selectedTextStyle : placeholderStyle, font()]
    }, selectedTextProps), isSelected !== null ? _lodash.default.get(currentValue, labelField) : placeholder), renderRightIcon ? renderRightIcon(visible) : /*#__PURE__*/_react.default.createElement(_reactNative.Image, {
      source: ic_down,
      style: _reactNative.StyleSheet.flatten([_styles.styles.icon, {
        tintColor: iconColor
      }, iconStyle])
    })));
  };
  const _renderItem = (0, _react.useCallback)(_ref => {
    let {
      item,
      index
    } = _ref;
    const isSelected = currentValue && _lodash.default.get(currentValue, valueField);
    const selected = _lodash.default.isEqual(_lodash.default.get(item, valueField), isSelected);
    _lodash.default.assign(item, {
      _index: index
    });
    return /*#__PURE__*/_react.default.createElement(_reactNative.TouchableHighlight, {
      key: index.toString(),
      testID: _lodash.default.get(item, itemTestIDField || labelField),
      accessible: !!accessibilityLabel,
      accessibilityLabel: _lodash.default.get(item, itemAccessibilityLabelField || labelField),
      underlayColor: activeColor,
      onPress: () => onSelect(item)
    }, /*#__PURE__*/_react.default.createElement(_reactNative.View, {
      style: _reactNative.StyleSheet.flatten([itemContainerStyle, selected && {
        backgroundColor: activeColor
      }])
    }, renderItem ? renderItem(item, selected) : /*#__PURE__*/_react.default.createElement(_reactNative.View, {
      style: _styles.styles.item
    }, /*#__PURE__*/_react.default.createElement(_reactNative.Text, {
      style: _reactNative.StyleSheet.flatten([_styles.styles.textItem, itemTextStyle, font()])
    }, _lodash.default.get(item, labelField)))));
  }, [accessibilityLabel, activeColor, currentValue, font, itemAccessibilityLabelField, itemContainerStyle, itemTestIDField, itemTextStyle, labelField, onSelect, renderItem, valueField]);
  const renderSearch = (0, _react.useCallback)(() => {
    if (search) {
      if (renderInputSearch) {
        return renderInputSearch(text => {
          if (onChangeText) {
            setSearchText(text);
            onChangeText(text);
          }
          onSearch(text);
        });
      } else {
        return /*#__PURE__*/_react.default.createElement(_TextInput.default, {
          testID: testID + ' input',
          accessibilityLabel: accessibilityLabel + ' input',
          style: [_styles.styles.input, inputSearchStyle],
          inputStyle: [inputSearchStyle, font()],
          value: searchText,
          autoCorrect: false,
          placeholder: searchPlaceholder,
          onChangeText: e => {
            if (onChangeText) {
              setSearchText(e);
              onChangeText(e);
            }
            onSearch(e);
          },
          placeholderTextColor: "gray",
          iconStyle: [{
            tintColor: iconColor
          }, iconStyle]
        });
      }
    }
    return null;
  }, [accessibilityLabel, font, iconColor, iconStyle, inputSearchStyle, onChangeText, onSearch, renderInputSearch, search, searchPlaceholder, testID, searchText]);
  const _renderList = (0, _react.useCallback)(isTopPosition => {
    const isInverted = !inverted ? false : isTopPosition;
    const _renderListHelper = () => {
      return /*#__PURE__*/_react.default.createElement(_reactNative.FlatList, _extends({
        testID: testID + ' flatlist',
        accessibilityLabel: accessibilityLabel + ' flatlist'
      }, flatListProps, {
        keyboardShouldPersistTaps: "handled",
        ref: refList,
        onScrollToIndexFailed: scrollIndex,
        data: listData,
        inverted: isTopPosition ? inverted : false,
        renderItem: _renderItem,
        keyExtractor: (_item, index) => index.toString(),
        showsVerticalScrollIndicator: showsVerticalScrollIndicator
      }));
    };
    return /*#__PURE__*/_react.default.createElement(_reactNative.TouchableWithoutFeedback, null, /*#__PURE__*/_react.default.createElement(_reactNative.View, {
      style: _styles.styles.flexShrink
    }, isInverted && _renderListHelper(), renderSearch(), !isInverted && _renderListHelper()));
  }, [_renderItem, accessibilityLabel, flatListProps, listData, inverted, renderSearch, scrollIndex, showsVerticalScrollIndicator, testID]);
  const _renderModal = (0, _react.useCallback)(() => {
    if (visible && position) {
      const {
        isFull,
        width,
        height,
        top,
        bottom,
        left
      } = position;
      const onAutoPosition = () => {
        if (keyboardHeight > 0) {
          return bottom < keyboardHeight + height;
        }
        return bottom < (search ? 150 : 100);
      };
      if (width && top && bottom) {
        const styleVertical = {
          left: left,
          maxHeight: maxHeight,
          minHeight: minHeight
        };
        const isTopPosition = dropdownPosition === 'auto' ? onAutoPosition() : dropdownPosition === 'top';
        let keyboardStyle = {};
        let extendHeight = !isTopPosition ? top : bottom;
        if (keyboardAvoiding && keyboardHeight > 0 && isTopPosition && dropdownPosition === 'auto') {
          extendHeight = keyboardHeight;
        }
        return /*#__PURE__*/_react.default.createElement(_reactNative.Modal, {
          transparent: true,
          statusBarTranslucent: true,
          visible: visible,
          supportedOrientations: ['landscape', 'portrait'],
          onRequestClose: showOrClose
        }, /*#__PURE__*/_react.default.createElement(_reactNative.TouchableWithoutFeedback, {
          onPress: showOrClose
        }, /*#__PURE__*/_react.default.createElement(_reactNative.View, {
          style: _reactNative.StyleSheet.flatten([_styles.styles.flex1, isFull && styleContainerVertical, backgroundColor && {
            backgroundColor: backgroundColor
          }, keyboardStyle])
        }, /*#__PURE__*/_react.default.createElement(_reactNative.View, {
          style: _reactNative.StyleSheet.flatten([_styles.styles.flex1, {
            width
          }, !isTopPosition ? {
            paddingTop: extendHeight
          } : {
            justifyContent: 'flex-end',
            paddingBottom: extendHeight
          }, isFull && _styles.styles.fullScreen])
        }, /*#__PURE__*/_react.default.createElement(_reactNative.View, {
          style: _reactNative.StyleSheet.flatten([_styles.styles.container, isFull ? styleHorizontal : styleVertical, containerStyle])
        }, _renderList(isTopPosition))))));
      }
      return null;
    }
    return null;
  }, [visible, search, position, keyboardHeight, maxHeight, minHeight, dropdownPosition, keyboardAvoiding, showOrClose, styleContainerVertical, backgroundColor, containerStyle, styleHorizontal, _renderList]);
  return /*#__PURE__*/_react.default.createElement(_reactNative.View, {
    style: _reactNative.StyleSheet.flatten([_styles.styles.mainWrap, style]),
    ref: ref,
    onLayout: _measure
  }, _renderDropdown(), _renderModal());
});
var _default = DropdownComponent;
exports.default = _default;
//# sourceMappingURL=index.js.map
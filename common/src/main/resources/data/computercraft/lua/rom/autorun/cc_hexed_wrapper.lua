local native = peripheral.call

local function translateTo(tbl)
	if type(tbl) == "table" then
		if getmetatable(tbl) ~= nil then
			if getmetatable(tbl).__name == "vector" then
				return { ["hexcasting:type"] = "hexcasting:vec3", ["hexcasting:data"] = { ["x"] = tbl.x, ["y"] = tbl.y, ["z"] = tbl.z } }
			elseif getmetatable(tbl).__name == "quaternion" then
				return { ["hexcasting:type"] = "complexhex:quaternion", ["hexcasting:data"] = { ["w"] = tbl.a, ["x"] = tbl.v.x, ["y"] = tbl.v.y, ["z"] = tbl.v.z } }
			elseif getmetatable(tbl).__name == "matrix" then
				local ret = { ["hexcasting:type"] = "moreiotas:matrix", ["hexcasting:data"] = { ["rows"] = tbl.rows, ["cols"] = tbl.columns, ["mat"] = {} } }
				for r = 1, tbl.rows do
					if not ret["hexcasting:data"].mat[r] then
						ret["hexcasting:data"].mat[r] = {}
					end
					for c = 1, tbl.columns do
						ret["hexcasting:data"].mat[r][c] = tbl[r][c]
					end
				end
				return ret
			end
		else
			for k,v in pairs(tbl) do
				tbl[k] = translateTo(v)
			end
		end
	end
	return tbl
end

local function translateFrom(tbl)
	if type(tbl) == "table" then
		if tbl["hexcasting:type"] == "hexcasting:vec3" then
			return vector.new(tbl["hexcasting:data"].x, tbl["hexcasting:data"].y, tbl["hexcasting:data"].z)
		elseif tbl["hexcasting:type"] == "complexhex:quaternion" then
			return quaternion.fromComponents(tbl["hexcasting:data"].x, tbl["hexcasting:data"].y, tbl["hexcasting:data"].z, tbl["hexcasting:data"].w)
		elseif tbl["hexcasting:type"] == "moreiotas:matrix" then
			return matrix.new(tbl["hexcasting:data"].rows, tbl["hexcasting:data"].cols, function(r,c) return tbl["hexcasting:data"].mat[r][c] end)
		else
			for k,v in pairs(tbl) do
				tbl[k] = translateFrom(v)
			end
		end
	end
	return tbl
end

peripheral.call = function(name, method, ...)
	if not peripheral.hasType(name, "wand") then
		return native(name, method, ...)
	end
	local args = table.pack(...)
	for k,v in pairs(args) do
		args[k] = translateTo(v)
	end
    return translateFrom(native(name, method, table.unpack(args)))
end